package at.fhtw.rest.service;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    public void save(DocumentEntity documentEntity) {
        documentRepository.save(documentEntity);
    }
}