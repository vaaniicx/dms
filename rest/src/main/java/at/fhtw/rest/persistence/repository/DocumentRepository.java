package at.fhtw.rest.persistence.repository;

import at.fhtw.rest.persistence.entity.DocumentEntity;

public interface DocumentRepository {
    void save(DocumentEntity document);
}