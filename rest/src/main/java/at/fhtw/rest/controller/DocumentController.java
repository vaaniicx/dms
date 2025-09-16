package at.fhtw.rest.controller;

import at.fhtw.rest.mapper.DocumentMapper;
import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.service.DocumentService;
import com.openapi.gen.springboot.api.DocumentApi;
import com.openapi.gen.springboot.dto.DocumentDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class DocumentController implements DocumentApi {
    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @Override
    public ResponseEntity<DocumentDto> uploadDocument(MultipartFile file) {

        DocumentEntity document = new DocumentEntity();
        document.setTitle(file.getOriginalFilename());

        DocumentEntity savedDocument = this.documentService.save(document);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentMapper.toDocument(savedDocument));
    }

    @Override
    public ResponseEntity<List<DocumentDto>> getDocuments() {
        return ResponseEntity.ok(documentMapper.toDocumentList(this.documentService.findAll()));
    }

    @Override
    public ResponseEntity<DocumentDto> getDocumentById(Long id) {
        return this.documentService.findById(id)
                .map(documentMapper::toDocument)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
