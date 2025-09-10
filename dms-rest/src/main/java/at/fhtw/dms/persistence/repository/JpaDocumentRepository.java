package at.fhtw.dms.persistence.repository;

import at.fhtw.dms.persistence.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDocumentRepository extends JpaRepository<DocumentEntity, Long> {
}
