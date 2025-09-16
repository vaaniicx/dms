package at.fhtw.rest.persistence.repository;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {
    private final JpaDocumentRepository repository;

    @Override
    public DocumentEntity save(DocumentEntity document) {
        log.trace("Repo.save({})", document);
        return repository.save(document);
    }

    @Override
    public List<DocumentEntity> findAll() {
        log.trace("Repo.findAll()");
        return repository.findAll();
    }

    @Override
    public Optional<DocumentEntity> findById(Long id) {
        log.trace("Repo.findById({})", id);
        return repository.findById(id);
    }

    @Override
    public void deleteAll() {
        log.warn("Repo.deleteAll()");
        repository.deleteAll();
    }
}
