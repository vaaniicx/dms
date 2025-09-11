package at.fhtw.rest.persistence.repository;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDocumentRepository extends JpaRepository<DocumentEntity, Long> {
}