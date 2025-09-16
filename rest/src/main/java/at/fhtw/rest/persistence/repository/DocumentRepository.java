package at.fhtw.rest.persistence.repository;

import at.fhtw.rest.persistence.entity.DocumentEntity;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {
    DocumentEntity save(DocumentEntity document);

    List<DocumentEntity> findAll();

    Optional<DocumentEntity> findById(Long id);
}
