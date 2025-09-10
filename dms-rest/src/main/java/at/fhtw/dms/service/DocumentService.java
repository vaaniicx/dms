package at.fhtw.dms.service;

import at.fhtw.dms.persistence.entity.DocumentEntity;
import at.fhtw.dms.persistence.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    // TODO: create and replace by business models
    public void save(DocumentEntity documentEntity) {
        documentRepository.save(documentEntity);
    }
}
