package at.fhtw.dms.controller;

import at.fhtw.dms.openapi.api.ApiDms;
import at.fhtw.dms.persistence.entity.DocumentEntity;
import at.fhtw.dms.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class DmsController implements ApiDms {

    private final DocumentService documentService;

    @Override
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Alive.");
    }

    @Override
    public ResponseEntity<Void> uploadDocument(MultipartFile file) {

        DocumentEntity document = new DocumentEntity();
        document.setTitle(file.getOriginalFilename());

        this.documentService.save(document);

        return ResponseEntity.ok().build();
    }
}
