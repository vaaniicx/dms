package at.fhtw.batch.repository;

import at.fhtw.batch.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.lastAccessed >= :since")
    List<Document> findDocumentsAccessedSince(@Param("since") LocalDateTime since);
}

