#!/bin/bash
set -e

print_status() { echo -e "$1"; }
print_success() { echo -e "\033[0;32m$1\033[0m"; }
print_warning() { echo -e "\033[1;33m$1\033[0m"; }
print_error() { echo -e "\033[0;31m$1\033[0m"; }

BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api/v1"
TEST_PDF_FILE="docs/rating-matrix.pdf"
UPLOADED_DOC_ID=""
COMPOSE_PROJECT="dms-integration-test"

cleanup() {
    print_status "Cleaning up..."
    docker-compose -p "$COMPOSE_PROJECT" down --remove-orphans >/dev/null 2>&1 || true
    print_success "Cleanup completed"
}
trap cleanup EXIT

print_status "Checking prerequisites..."

if ! docker info >/dev/null 2>&1; then
    print_error "Docker is not running"
    exit 1
fi

if [ ! -f "docker-compose.yml" ]; then
    print_error "docker-compose.yml not found"
    exit 1
fi

if [ ! -f ".env" ]; then
    print_error ".env file not found and no .env.example available"
    exit 1
fi

print_status "Starting Docker stack..."
docker-compose -p "$COMPOSE_PROJECT" up -d

print_status "Waiting for Ollama API and llama3 model to be ready..."
start_time=$(date +%s)
max_wait=900  # 15 minutes
ollama_url="http://localhost:11434"

while true; do
    current_time=$(date +%s)
    elapsed=$((current_time - start_time))
    
    if [ $elapsed -gt $max_wait ]; then
        print_warning "Ollama timeout reached, continuing with tests..."
        break
    fi
    
    # Ollama API is responding?
    if curl -s --max-time 5 "$ollama_url/api/version" >/dev/null 2>&1; then
        print_status "Ollama API is responding, checking for llama3 model..."
        
        # llama3 model available?
        if models_response=$(curl -s --max-time 10 "$ollama_url/api/tags" 2>/dev/null); then
            if echo "$models_response" | grep -q "llama3"; then
                print_success "llama3 model available!"
                break
            else
                print_status "llama3 model still downloading..."
            fi
        else
            print_status "Ollama API responding but tags endpoint not ready..."
        fi
    else
        print_status "Waiting for Ollama API to start..."
    fi
    
    printf "."
    sleep 10
done

echo ""

print_status "Waiting for DMS REST API to be ready..."
max_attempts=60
attempt=0

while [ $attempt -lt $max_attempts ]; do
    if curl -s "$API_BASE/status" >/dev/null 2>&1; then
        print_success "DMS REST API is ready"
        break
    fi
    
    attempt=$((attempt + 1))
    printf "."
    sleep 5
done

if [ $attempt -eq $max_attempts ]; then
    print_error "DMS REST API did not become ready within timeout"
    exit 1
fi

echo ""

TEST_PDF_FILE="docs/rating-matrix.pdf"
print_success "Using: $TEST_PDF_FILE"

test_api_health() {
    print_status "GET /api/v1/status"
    
    response=$(curl -s -w "%{http_code}" "$API_BASE/status")
    http_code="${response: -3}"
    body="${response%???}"
    
    if [ "$http_code" = "200" ]; then
        print_success "API health check passed"
        echo "   Response: $body"
    else
        print_error "API health check failed (HTTP $http_code)"
        return 1
    fi
}

test_document_upload() {
    print_status "POST /api/v1/documents"
    
    full_response=$(curl -s -i -X POST -F "file=@$TEST_PDF_FILE" "$API_BASE/documents")
    http_code=$(echo "$full_response" | grep "HTTP/" | tail -1 | awk '{print $2}')
    
    if [ "$http_code" = "201" ]; then
        location_header=$(echo "$full_response" | grep -i "location:" | cut -d' ' -f2 | tr -d '\r\n' || true)
        
        if [ -n "$location_header" ]; then
            UPLOADED_DOC_ID=$(echo "$location_header" | sed 's/.*\///')
            print_success "Document uploaded successfully with ID: $UPLOADED_DOC_ID"
        else
            UPLOADED_DOC_ID=$(curl -s "$API_BASE/documents" | jq -r '.[0].id // empty')
            if [ -n "$UPLOADED_DOC_ID" ]; then
                print_success "Document uploaded successfully with ID: $UPLOADED_DOC_ID"
            else
                print_error "No Location header and could not determine document ID"
                return 1
            fi
        fi
    else
        print_error "Document upload failed (HTTP $http_code)"
        if [ -n "$response_body" ]; then
            echo "   Response body: $response_body"
        fi
        return 1
    fi
}

wait_for_document_processing() {
    if [ -z "$UPLOADED_DOC_ID" ]; then
        return 1
    fi
    
    print_status "Waiting for document processing (OCR → Summarization → Indexing)..."
    local max_wait=300  # 5 minutes
    local start_time=$(date +%s)
    
    while true; do
        local current_time=$(date +%s)
        local elapsed=$((current_time - start_time))
        
        if [ $elapsed -gt $max_wait ]; then
            print_warning "Document processing timeout after ${max_wait}s - proceeding with tests"
            break
        fi
        
        # Check if document exists and get its status
        local doc_response=$(curl -s "$API_BASE/documents/$UPLOADED_DOC_ID" 2>/dev/null || echo "")
        if echo "$doc_response" | jq -e '.id? != null' >/dev/null; then
            # check if we can search for it (indexed)
            local search_response=$(curl -s "$API_BASE/documents/search?query=rating" 2>/dev/null || echo "")
            if echo "$search_response" | jq -e --arg id "$UPLOADED_DOC_ID" 'any(.. | objects | .id?; tostring == $id)' >/dev/null; then
                print_success "Document processing completed and indexed"
                return 0
            fi
        fi
        
        printf "."
        sleep 10
    done
    
    echo
    return 0
}

test_document_retrieval() {
    print_status "GET /api/v1/documents/$UPLOADED_DOC_ID"
    
    if [ -z "$UPLOADED_DOC_ID" ]; then
        print_error "No document ID available for retrieval test"
        return 1
    fi
    
    response=$(curl -s -w "%{http_code}" "$API_BASE/documents/$UPLOADED_DOC_ID")
    http_code="${response: -3}"
    body="${response%???}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Document retrieval successful"
        
        document_id=$(echo "$body" | jq -r '.id // empty')
        file_name=$(echo "$body" | jq -r '.file.name // empty')
        echo "   Document ID: $document_id"
        echo "   File name: $file_name"
    else
        print_error "Document retrieval failed (HTTP $http_code)"
        return 1
    fi
}

test_document_download() {
    print_status "GET /api/v1/documents/$UPLOADED_DOC_ID/download"
    
    if [ -z "$UPLOADED_DOC_ID" ]; then
        print_error "No document ID available for download test"
        return 1
    fi
    
    response=$(curl -s -w "%{http_code}" \
        -o "downloaded_test.pdf" \
        "$API_BASE/documents/$UPLOADED_DOC_ID/download")
    
    http_code="${response: -3}"
    
    if [ "$http_code" = "200" ] && [ -f "downloaded_test.pdf" ] && [ -s "downloaded_test.pdf" ]; then
        file_size=$(wc -c < "downloaded_test.pdf")
        print_success "Document download successful ($file_size bytes)"
        rm -f "downloaded_test.pdf"
    else
        print_error "Document download failed (HTTP $http_code)"
        return 1
    fi
}

test_document_search() {
    print_status "GET /api/v1/documents/search?query=test&scope=name"
    
    response=$(curl -s -w "%{http_code}" \
        "$API_BASE/documents/search?query=test&scope=name")
    
    http_code="${response: -3}"
    body="${response%???}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Document search successful"
        
        count=$(echo "$body" | jq '. | length')
        echo "   Found $count documents"
    else
        print_error "Document search failed (HTTP $http_code)"
        return 1
    fi
}

test_get_all_documents() {
    print_status "GET /api/v1/documents"
    
    response=$(curl -s -w "%{http_code}" "$API_BASE/documents")
    http_code="${response: -3}"
    body="${response%???}"
    
    if [ "$http_code" = "200" ]; then
        print_success "Get all documents successful"
        
        count=$(echo "$body" | jq '. | length')
        echo "   Total documents: $count"
    else
        print_error "Get all documents failed (HTTP $http_code)"
        return 1
    fi
}

test_document_deletion() {
    print_status "DELETE /api/v1/documents/$UPLOADED_DOC_ID"
    
    if [ -z "$UPLOADED_DOC_ID" ]; then
        print_error "No document ID available for deletion test"
        return 1
    fi
    
    # Delete document
    response=$(curl -s -w "%{http_code}" \
        -X DELETE \
        "$API_BASE/documents/$UPLOADED_DOC_ID")
    
    http_code="${response: -3}"
    
    if [ "$http_code" = "204" ]; then
        # Verify deletion by trying to retrieve
        verify_response=$(curl -s -w "%{http_code}" "$API_BASE/documents/$UPLOADED_DOC_ID")
        verify_code="${verify_response: -3}"
        
        if [ "$verify_code" = "404" ]; then
            print_success "Document deletion successful and verified"
        else
            print_error "Document deletion not verified (HTTP $verify_code)"
            return 1
        fi
    else
        print_error "Document deletion failed (HTTP $http_code)"
        return 1
    fi
}

# Run all tests
print_status "Starting integration tests..."

tests_passed=0
tests_total=7

run_test() {
    if $1; then
        tests_passed=$((tests_passed + 1))
        # If upload test passed, wait for processing
        if [ "$1" = "test_document_upload" ]; then
            wait_for_document_processing
        fi
    fi
}

run_test test_api_health
run_test test_document_upload
run_test test_document_retrieval
run_test test_document_download
run_test test_document_search
run_test test_get_all_documents
run_test test_document_deletion

echo ""
print_status "Integration Test Results"
print_status "========================"

if [ $tests_passed -eq $tests_total ]; then
    print_success "All $tests_total tests passed!"
    exit 0
else
    print_error "Only $tests_passed out of $tests_total tests passed"
    print_warning "Press Enter to continue with cleanup..."
    read -r
    exit 1
fi
