package at.fhtw.rest.controller;

import at.fhtw.rest.core.persistence.entity.Document;
import at.fhtw.rest.core.service.DocumentServiceImpl;
import at.fhtw.rest.core.service.ElasticsearchService;
import at.fhtw.rest.core.util.DownloadableDocument;
import at.fhtw.rest.mapper.document.DocumentMapper;
import com.openapi.gen.springboot.api.DocumentApi;
import com.openapi.gen.springboot.dto.DocumentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
public class DocumentController implements DocumentApi {

    private final DocumentServiceImpl documentService;

    private final ElasticsearchService elasticsearchService;

    private final DocumentMapper documentMapper;

    @Override
    public ResponseEntity<List<DocumentResponse>> getDocuments() {
        List<Document> allDocuments = documentService.getAllDocuments();
        List<DocumentResponse> response = allDocuments.stream().map(documentMapper::mapToResponse).toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DocumentResponse> getDocument(Long id) {
        Document document = documentService.getDocumentById(id);
        DocumentResponse response = documentMapper.mapToResponse(document);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> uploadDocument(MultipartFile file) {
        Document document = documentService.saveDocument(file);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(document.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    // todo: refactor this and/or move it into service
    @Override
    public ResponseEntity<List<DocumentResponse>> searchDocuments(String query, String scope) {
        String effectiveScope = scope == null || scope.isBlank() ? "content" : scope.toLowerCase();

        if ("name".equals(effectiveScope)) {
            List<Document> documents = documentService.searchByFileName(query);
            List<DocumentResponse> response = documents.stream().map(documentMapper::mapToResponse).toList();
            return ResponseEntity.ok(response);
        }

        var contentIds = elasticsearchService.searchDocumentIds(query);

        if (contentIds.isEmpty()) {
            if ("all".equals(effectiveScope)) {
                // No content hits, but maybe there are filename matches
                List<Document> documents = documentService.searchByFileName(query);
                List<DocumentResponse> response = documents.stream().map(documentMapper::mapToResponse).toList();
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(List.of());
        }

        if (!"all".equals(effectiveScope)) {
            List<Document> allDocuments = documentService.getAllDocuments();
            List<DocumentResponse> matchedDocumentsByContent = allDocuments.stream()
                    .filter(document -> contentIds.contains(document.getId()))
                    .map(documentMapper::mapToResponse)
                    .toList();
            return ResponseEntity.ok(matchedDocumentsByContent);
        }

        // scope == "all": combine content + filename hits
        List<Document> matchedDocumentsByName = documentService.searchByFileName(query);
        List<Long> nameIds = matchedDocumentsByName.stream()
                .map(Document::getId)
                .toList();

        // Preserve a stable order: by insertedAt or id via the base list
        List<Document> combined = documentService.getAllDocuments().stream()
                .filter(dto -> contentIds.contains(dto.getId()) || nameIds.contains(dto.getId()))
                .toList();
        List<DocumentResponse> responses = combined.stream().map(documentMapper::mapToResponse).toList();
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<Void> deleteDocument(Long id) {
        documentService.deleteDocumentById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(Long id, Boolean inline) {
        DownloadableDocument document = documentService.download(id);
        String disposition = (inline != null && inline) ? "inline" : "attachment";

        return ResponseEntity.ok()
                .header("Content-Disposition", disposition + "; filename=\"" + document.fileName() + "\"")
                .contentType(MediaType.parseMediaType(document.contentType()))
                .body(document.stream());
    }
}
