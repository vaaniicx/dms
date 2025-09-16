package at.fhtw.rest.service;

import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentEntity save(DocumentEntity documentEntity) {
        log.debug("Service.save(title='{}')", documentEntity.getTitle());
        var saved = documentRepository.save(documentEntity);
        log.info("Saved document id={}", saved.getId());
        return saved;
    }

    public List<DocumentEntity> findAll() {
        log.debug("Service.findAll()");
        return documentRepository.findAll();
    }

    public Optional<DocumentEntity> findById(Long id) {
        log.debug("Service.findById({})", id);
        return documentRepository.findById(id);
    }
}
