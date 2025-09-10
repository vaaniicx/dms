package at.fhtw.dms.persistence.repository;

import at.fhtw.dms.persistence.entity.DocumentEntity;

public interface DocumentRepository {

    void save(DocumentEntity document);
}
