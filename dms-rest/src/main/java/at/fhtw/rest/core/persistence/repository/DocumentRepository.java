package at.fhtw.rest.core.persistence.repository;

import at.fhtw.rest.core.persistence.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.documentFile.name LIKE ?1")
    List<Document> findByFileName(String fileNamePart);
}
