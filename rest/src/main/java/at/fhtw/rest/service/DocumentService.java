package at.fhtw.rest.service;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository repository;

    public DocumentEntity save(DocumentEntity document) {
        return repository.save(document);
    }

    public List<DocumentEntity> findAll() {
        return repository.findAll();
    }

    public Optional<DocumentEntity> findById(Long id) {
        return repository.findById(id);
    }
}
