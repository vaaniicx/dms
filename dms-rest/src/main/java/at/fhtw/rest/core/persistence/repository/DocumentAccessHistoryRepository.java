package at.fhtw.rest.core.persistence.repository;

import at.fhtw.rest.core.persistence.entity.DocumentAccessHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentAccessHistoryRepository extends JpaRepository<DocumentAccessHistory, Long> {

    List<DocumentAccessHistory> findByDocumentId(Long documentId);
}

