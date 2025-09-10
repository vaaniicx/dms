package at.fhtw.dms.persistence.repository;

import at.fhtw.dms.persistence.entity.DocumentEntity;
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
