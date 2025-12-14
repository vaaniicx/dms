package at.fhtw.rest.controller;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.service.DocumentService;
import at.fhtw.rest.service.DownloadableDocument;
import at.fhtw.rest.service.ElasticsearchSearchService;
import com.openapi.gen.springboot.api.DocumentApi;
import com.openapi.gen.springboot.dto.DocumentDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
public class DocumentController implements DocumentApi {
    private final DocumentService service;
    private final ElasticsearchSearchService searchService;

    @Override
    public ResponseEntity<Void> uploadDocument(MultipartFile file) {
        DocumentEntity saved = service.save(file);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<DocumentDto>> getDocuments() {
        return ResponseEntity.ok(service.findAll());
    }

    @Override
    public ResponseEntity<List<DocumentDto>> searchDocuments(String query, String scope) {
        String effectiveScope = scope == null || scope.isBlank() ? "content" : scope.toLowerCase();

        if ("name".equals(effectiveScope)) {
            return ResponseEntity.ok(service.searchByFileName(query));
        }

        var contentIds = searchService.searchDocumentIds(query);

        if (contentIds.isEmpty()) {
            if ("all".equals(effectiveScope)) {
                // No content hits, but maybe there are filename matches
                return ResponseEntity.ok(service.searchByFileName(query));
            }
            return ResponseEntity.ok(List.of());
        }

        if (!"all".equals(effectiveScope)) {
            List<DocumentDto> contentMatches = service.findAll().stream()
                    .filter(dto -> contentIds.contains(dto.getId()))
                    .toList();
            return ResponseEntity.ok(contentMatches);
        }

        // scope == "all": combine content + filename hits
        List<DocumentDto> nameMatches = service.searchByFileName(query);

        List<Long> nameIds = nameMatches.stream()
                .map(DocumentDto::getId)
                .toList();

        List<DocumentDto> allDocuments = service.findAll();

        // Preserve a stable order: by insertedAt or id via the base list
        List<DocumentDto> combined = allDocuments.stream()
                .filter(dto -> contentIds.contains(dto.getId()) || nameIds.contains(dto.getId()))
                .toList();

        return ResponseEntity.ok(combined);
    }

    @Override
    public ResponseEntity<DocumentDto> getDocumentById(Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }

    @Override
    public ResponseEntity<Void> deleteDocument(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(Long id, Boolean inline) {
        DownloadableDocument document = service.download(id);
        String disposition = (inline != null && inline) ? "inline" : "attachment";

        return ResponseEntity.ok()
                .header("Content-Disposition", disposition + "; filename=\"" + document.fileName() + "\"")
                .contentType(MediaType.parseMediaType(document.contentType()))
                .body(document.stream());
    }
}
