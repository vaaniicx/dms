package at.fhtw.rest.service;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentEntity save(DocumentEntity documentEntity) {
        return documentRepository.save(documentEntity);
    }

    public List<DocumentEntity> findAll() {
        return documentRepository.findAll();
    }

    public Optional<DocumentEntity> findById(Long id) {
        return documentRepository.findById(id);
    }
}
