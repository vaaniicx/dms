package at.fhtw.ocr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper;

    @Value("${elasticsearch.url:http://localhost:9200}")
    private String elasticsearchUrl;

    @Value("${elasticsearch.index:documents}")
    private String indexName;

    public void indexDocument(long documentId, String fileContent) {
        String resolvedIndex = getResolvedIndex();
        URI uri = URI.create(String.format("%s/%s/_doc/%d", elasticsearchUrl, resolvedIndex, documentId));

        String body = buildRequestBody(documentId, fileContent);
        HttpRequest indexRequest = buildIndexRequest(uri, body);

        requestIndexing(documentId, indexRequest);

        log.info("Indexed document {} in Elasticsearch index '{}'", documentId, resolvedIndex);
    }

    private String buildRequestBody(long documentId, String fileContent) {
        Map<String, Object> payload = getPayload(documentId, fileContent);
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            // todo: error handling
            throw new RuntimeException(e);
        }
    }

    private static HttpRequest buildIndexRequest(URI uri, String body) {
        return HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    private void requestIndexing(long documentId, HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.error("Failed to index document {} in Elasticsearch. Status: {}, Body: {}",
                        documentId, response.statusCode(), response.body());
                throw new IllegalStateException("Failed to index document in Elasticsearch");
            }
        } catch (Exception e) {
            // todo: error handling
            return;
        }
    }

    private Map<String, Object> getPayload(long documentId, String fileContent) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("documentId", documentId);
        payload.put("content", fileContent);
        return payload;
    }

    private String getResolvedIndex() {
        if (!(indexName == null || indexName.isBlank())) {
            return indexName;
        }
        return "documents";
    }
}
