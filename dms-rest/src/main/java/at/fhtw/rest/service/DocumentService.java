package at.fhtw.rest.service;

import at.fhtw.message.document.DocumentUploadedMessage;
import at.fhtw.rest.exception.DocumentMessagingException;
import at.fhtw.rest.exception.DocumentNotFoundException;
import at.fhtw.rest.mapper.DocumentMapper;
import at.fhtw.rest.message.publisher.MessagePublisher;
import at.fhtw.rest.persistence.entity.DocumentEntity;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import at.fhtw.rest.persistence.storage.DocumentStorage;
import com.openapi.gen.springboot.dto.DocumentDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {

    private static final Tika TIKA = new Tika();

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    private final DocumentStorage documentStorage;

    private final DocumentMetadata metadataExtractor;

    private final MessagePublisher messagePublisher;

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

        String objectKey = documentStorage.store(file, mimeType.split("/")[1]);

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

        DocumentEntity document = builder
            .uploaded(true)
            .build();
        DocumentEntity savedDocument = documentRepository.save(document);

        DocumentUploadedMessage documentUploadedMessage = new DocumentUploadedMessage(savedDocument.getId(), objectKey);
        try {
            messagePublisher.publishDocumentUploaded(documentUploadedMessage);
        } catch (DocumentMessagingException e) {
            throw new DocumentMessagingException("Failed to enqueue document " + savedDocument.getId(), e);
        }

        return savedDocument;
    }

    public List<DocumentDto> findAll() {
        return documentMapper.toDocumentList(documentRepository.findAll());
    }

    public Optional<DocumentDto> findById(Long id) {
        return documentRepository.findById(id).map(documentMapper::toDocument);
    }

    public void delete(Long id) {
        DocumentEntity entity = documentRepository.findById(id).orElseThrow(() ->
                new DocumentNotFoundException("Document not found"));
        if (entity.getObjectKey() != null) {
            documentStorage.delete(entity.getObjectKey());
        }
        documentRepository.deleteById(id);
    }

    public DownloadableDocument download(Long id) {
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));

        if (document.getObjectKey() == null) {
            throw new DocumentNotFoundException("No object stored for document");
        }

        InputStream inputStream = documentStorage.load(document.getObjectKey());
        return new DownloadableDocument(new InputStreamResource(inputStream),
                document.getFileName() != null ? document.getFileName() : "document",
                document.getFileType() != null ? document.getFileType() : "application/octet-stream");
    }
}
