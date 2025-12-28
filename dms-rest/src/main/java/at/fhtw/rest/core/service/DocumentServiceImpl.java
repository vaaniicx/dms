package at.fhtw.rest.core.service;

import at.fhtw.message.document.DocumentUploadedMessage;
import at.fhtw.rest.core.exception.DocumentMessagingException;
import at.fhtw.rest.core.exception.DocumentNotFoundException;
import at.fhtw.rest.core.persistence.entity.Document;
import at.fhtw.rest.core.persistence.entity.DocumentFile;
import at.fhtw.rest.core.persistence.entity.DocumentStatus;
import at.fhtw.rest.core.persistence.entity.DocumentStatusHistory;
import at.fhtw.rest.core.persistence.repository.DocumentRepository;
import at.fhtw.rest.core.persistence.storage.DocumentStorage;
import at.fhtw.rest.core.util.DocumentMetadata;
import at.fhtw.rest.core.util.DocumentMetadataExtractor;
import at.fhtw.rest.core.util.DownloadableDocument;
import at.fhtw.rest.message.publisher.MessagePublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private static final Tika TIKA = new Tika();

    private final DocumentRepository documentRepository;

    private final DocumentStorage documentStorage;

    private final MessagePublisher messagePublisher;

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));
    }

    @Override
    public List<Document> getDocumentsByFileName(String fileName) {
        return documentRepository.findByFileName(fileName);
    }

    @Override
    public void updateSummary(Long id, String summary) {
        Document document = getDocumentById(id);
        document.setSummary(summary);
        documentRepository.save(document);
    }

    @Override
    public void updateDocumentStatus(Long id, DocumentStatus status) {
        Document document = getDocumentById(id);
        document.getStatusHistory().add(new DocumentStatusHistory(status));
        documentRepository.save(document);
    }

    @Override
    public void deleteDocumentById(Long id) {
        Document document = getDocumentById(id);
        documentRepository.delete(document);
    }

    @Override
    public Document saveDocument(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No file provided");
        }

        String mimeType;
        try {
            mimeType = TIKA.detect(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            throw new UnsupportedMediaTypeStatusException("Failed to detect MIME type");
        }

        if (!"application/pdf".equalsIgnoreCase(mimeType)) {
            throw new UnsupportedMediaTypeStatusException("Expected application/pdf but got " + mimeType);
        }

        String objectKey = documentStorage.store(file, mimeType.split("/")[1]);

        DocumentMetadata metadata = DocumentMetadataExtractor.extract(file);

        Document document = Document.builder()
                .title(metadata.getTitle())
                .author(metadata.getAuthor())
                .documentFile(DocumentFile.builder()
                        .objectKey(objectKey)
                        .name(file.getOriginalFilename())
                        .type(mimeType)
                        .pageCount(metadata.getPageCount())
                        .size(file.getSize())
                        .creationDate(metadata.getCreationDate())
                        .modificationDate(metadata.getModificationDate())
                        .build())
                .statusHistory(Collections.singletonList(new DocumentStatusHistory(DocumentStatus.UPLOADED)))
                .build();

        Document savedDocument = documentRepository.save(document);

        // TODO: extract into publisher
        DocumentUploadedMessage documentUploadedMessage = new DocumentUploadedMessage(savedDocument.getId(), objectKey);
        try {
            messagePublisher.publishDocumentUploaded(documentUploadedMessage);
        } catch (DocumentMessagingException e) {
            throw new DocumentMessagingException("Failed to enqueue document " + savedDocument.getId(), e);
        }

        return savedDocument;
    }

    public List<Document> searchByFileName(String query) {
        return documentRepository.findByFileName(query);
    }

    public DownloadableDocument download(Long id) {
        Document document = getDocumentById(id);

        String objectKey = document.getDocumentFile().getObjectKey();
        if (objectKey == null) {
            throw new DocumentNotFoundException("No object stored for document");
        }

        InputStream inputStream = documentStorage.load(objectKey);
        return new DownloadableDocument(new InputStreamResource(inputStream),
                document.getDocumentFile().getName() != null ? document.getDocumentFile().getName() : "document",
                document.getDocumentFile().getType() != null ? document.getDocumentFile().getType() : "application/octet-stream");
    }
}
