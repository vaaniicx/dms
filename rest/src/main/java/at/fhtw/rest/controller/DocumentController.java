package at.fhtw.rest.controller;

import at.fhtw.rest.mapper.DocumentMapper;
import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.service.DocumentService;
import com.openapi.gen.springboot.api.DocumentApi;
import com.openapi.gen.springboot.dto.DocumentDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
public class DocumentController implements DocumentApi {
    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @Override
    public ResponseEntity<DocumentDto> uploadDocument(MultipartFile file) {
        String name = file != null ? file.getOriginalFilename() : null;
        long size = file != null ? file.getSize() : -1;
        log.info("POST /documents (upload) name='{}', size={}", name, size);

        if (file == null || file.isEmpty()) {
            log.warn("Upload rejected: file is null/empty");
            return ResponseEntity.badRequest().build();
        }

        DocumentEntity document = new DocumentEntity();
        document.setTitle(name);

        DocumentEntity savedDocument = this.documentService.save(document);
        log.info("Document created id={}, title='{}'", savedDocument.getId(), savedDocument.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentMapper.toDocument(savedDocument));
    }

    @Override
    public ResponseEntity<List<DocumentDto>> getDocuments() {
        log.info("GET /documents");
        var entities = this.documentService.findAll();
        log.debug("Returning {} documents", entities.size());
        return ResponseEntity.ok(documentMapper.toDocumentList(entities));
    }

    @Override
    public ResponseEntity<DocumentDto> getDocumentById(Long id) {
        log.info("GET /documents/{}", id);
        return this.documentService.findById(id)
                .map(entity -> {
                    log.debug("Found document id={}, title='{}'", entity.getId(), entity.getTitle());
                    return ResponseEntity.ok(documentMapper.toDocument(entity));
                })
                .orElseGet(() -> {
                    log.warn("Document {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
