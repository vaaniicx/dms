package at.fhtw.rest.core.service;

import at.fhtw.rest.core.persistence.entity.Document;
import at.fhtw.rest.core.persistence.entity.DocumentStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    List<Document> getAllDocuments();

    List<Document> getDocumentsByFileName(String fileName);

    Document getDocumentById(Long id);

    Document saveDocument(MultipartFile file);

    void updateSummary(Long id, String summary);

    void updateDocumentStatus(Long id, DocumentStatus status);

    void deleteDocumentById(Long id);
}
