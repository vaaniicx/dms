package at.fhtw.rest.persistence.repository;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {
    private final JpaDocumentRepository jpaDocumentRepository;

    @Override
    public void save(DocumentEntity document) {
        jpaDocumentRepository.save(document);
    }
}