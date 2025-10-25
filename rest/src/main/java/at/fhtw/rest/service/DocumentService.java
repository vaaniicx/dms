package at.fhtw.rest.service;

import at.fhtw.rest.exception.DocumentMessagingException;
import at.fhtw.rest.mapper.DocumentMapper;
import at.fhtw.rest.messaging.DocumentPublisher;
import at.fhtw.rest.messaging.dto.DocumentMessage;
import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import at.fhtw.rest.persistence.storage.DocumentStorage;
import com.openapi.gen.springboot.dto.DocumentDto;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import at.fhtw.rest.exception.DocumentNotFoundException;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository repository;
    private final DocumentMapper mapper;
    private final DocumentPublisher publisher;
    private final DocumentStorage storage;
    private final DocumentMetadata metadataExtractor;
    private static final Tika TIKA = new Tika();

    public DocumentEntity save(MultipartFile file) {
        if (file.isEmpty()) {
            log.error("No file provided");
            throw new IllegalArgumentException("No file provided");
        }

        String mimeType;
        try {
            mimeType = TIKA.detect(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            log.error("Failed to detect MIME type", e);
            throw new UnsupportedMediaTypeStatusException("Failed to detect MIME type");
        }

        if (!"application/pdf".equalsIgnoreCase(mimeType)) {
            log.error("Unsupported media type: {}", mimeType);
            throw new UnsupportedMediaTypeStatusException("Expected application/pdf but got " + mimeType);
        }

        Optional<DocumentMetadata.PdfMetadata> metadata = metadataExtractor.extract(file);

        String objectKey = storage.store(file, mimeType.split("/")[1]);

        DocumentEntity.DocumentEntityBuilder builder = DocumentEntity.builder()
            .fileType(mimeType)
            .fileName(file.getOriginalFilename())
            .fileSize(file.getSize())
            .objectKey(objectKey);

        metadata.ifPresent(m -> builder
            .docPageCount(m.pageCount())
            .docTitle(m.title())
            .docAuthor(m.author())
            .docCreatedAt(m.createdAt())
            .docUpdatedAt(m.updatedAt())
        );

        DocumentEntity document = builder.build();
        DocumentEntity saved = repository.save(document);

        try {
            byte[] content = file.getBytes();
            DocumentMessage message = DocumentMessage.from(saved.getId(), content);
            publisher.send(message);
        } catch (IOException e) {
            log.error("Failed to read file content for document {}", saved.getId(), e);
            throw new at.fhtw.rest.exception.DocumentStorageException("Failed to read file content", e);
        } catch (DocumentMessagingException e) {
            throw new DocumentMessagingException("Failed to enqueue document " + saved.getId(), e);
        }

        return saved;
    }

    public List<DocumentDto> findAll() {
        return mapper.toDocumentList(repository.findAll());
    }

    public Optional<DocumentDto> findById(Long id) {
        return repository.findById(id).map(mapper::toDocument);
    }

    public void delete(Long id) {
        DocumentEntity entity = repository.findById(id).orElseThrow(() ->
            new DocumentNotFoundException("Document not found"));
        if (entity.getObjectKey() != null) {
            storage.delete(entity.getObjectKey());
        }
        repository.deleteById(id);
    }
}
