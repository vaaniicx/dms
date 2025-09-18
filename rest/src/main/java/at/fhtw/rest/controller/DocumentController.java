package at.fhtw.rest.controller;

import at.fhtw.rest.mapper.DocumentMapper;
import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.service.DocumentService;
import com.openapi.gen.springboot.api.DocumentApi;
import com.openapi.gen.springboot.dto.DocumentDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final DocumentService service;
    private final DocumentMapper mapper;

    @Override
    public ResponseEntity<Void> uploadDocument(MultipartFile file) {
        DocumentEntity entity = DocumentEntity.builder()
                .filename(file.getOriginalFilename())
                .build();

        DocumentEntity saved = service.save(entity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @Override
    public ResponseEntity<List<DocumentDto>> getDocuments() {
        return ResponseEntity.ok(mapper.toDocumentList(service.findAll()));
    }

    @Override
    public ResponseEntity<DocumentDto> getDocumentById(Long id) {
        return this.service.findById(id)
                .map(mapper::toDocument)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
