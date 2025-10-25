package at.fhtw.rest.controller;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.service.DocumentService;
import com.openapi.gen.springboot.api.DocumentApi;
import com.openapi.gen.springboot.dto.DocumentDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
public class DocumentController implements DocumentApi {
    private final DocumentService service;

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
    public ResponseEntity<DocumentDto> getDocumentById(Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
    }

    @DeleteMapping("/api/v1/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
