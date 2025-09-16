package at.fhtw.rest.persistence.repository;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {
    private final JpaDocumentRepository repository;

    @Override
    public DocumentEntity save(DocumentEntity document) {
        return repository.save(document);
    }

    @Override
    public List<DocumentEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<DocumentEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
