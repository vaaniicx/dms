package at.fhtw.rest.controller;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.service.DocumentService;
import com.openapi.gen.springboot.api.DocumentApi;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class DocumentController implements DocumentApi {
    private final DocumentService documentService;

    @Override
    public ResponseEntity<Void> uploadDocument(MultipartFile file) {

        DocumentEntity document = new DocumentEntity();
        document.setTitle(file.getOriginalFilename());

        this.documentService.save(document);

        return ResponseEntity.ok().build();
    }
}