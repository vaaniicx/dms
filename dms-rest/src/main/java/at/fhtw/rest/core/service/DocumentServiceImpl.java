package at.fhtw.rest.core.service;

import at.fhtw.rest.core.exception.DocumentNotFoundException;
import at.fhtw.rest.core.persistence.entity.*;
import at.fhtw.rest.core.persistence.repository.DocumentRepository;
import at.fhtw.rest.core.persistence.storage.DocumentStorage;
import at.fhtw.rest.core.util.DocumentMetadata;
import at.fhtw.rest.core.util.DocumentMetadataExtractor;
import at.fhtw.rest.core.util.DownloadableDocument;
import at.fhtw.rest.message.publisher.MessagePublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

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
    public void updateDocumentStatus(Long id, DocumentStatusHistory statusHistory) {
        Document document = getDocumentById(id);
        document.getStatusHistory().add(statusHistory);
        documentRepository.save(document);
    }

    @Override
    public void updateDocumentAccessHistory(Long id, DocumentAccessHistory accessHistory) {
        Document document = getDocumentById(id);
        document.getAccessHistory().add(accessHistory);
        documentRepository.save(document);
    }

    @Override
    public void deleteDocumentById(Long id) {
        Document document = getDocumentById(id);
        documentRepository.delete(document);
    }

    @Override
    public Document saveDocument(MultipartFile file) {
        ensureFileIsPresent(file);
        ensureFileIsPdf(file);

        String objectKey = documentStorage.store(file);

        DocumentMetadata metadata = DocumentMetadataExtractor.extract(file);
        Document document = buildDocument(file, metadata, objectKey);

        Document persistedDocument = documentRepository.save(document);

        messagePublisher.publishDocumentUploaded(persistedDocument.getId(), objectKey);

        return persistedDocument;
    }

    private void ensureFileIsPresent(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No file provided");
        }
    }

    private void ensureFileIsPdf(MultipartFile file) {
        if (file.getContentType() == null || !"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only application/pdf files are supported");
        }
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

    private static Document buildDocument(MultipartFile file, DocumentMetadata metadata, String objectKey) {
        return Document.builder()
                .title(metadata.getTitle())
                .author(metadata.getAuthor())
                .documentFile(DocumentFile.builder()
                        .objectKey(objectKey)
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .pageCount(metadata.getPageCount())
                        .size(file.getSize())
                        .creationDate(metadata.getCreationDate())
                        .modificationDate(metadata.getModificationDate())
                        .build())
                .statusHistory(Collections.singletonList(new DocumentStatusHistory(DocumentStatus.UPLOADED)))
                .build();
    }
}
