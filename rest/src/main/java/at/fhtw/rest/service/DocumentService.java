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
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;
    private final DocumentMapper mapper;
    private final DocumentPublisher publisher;
    private final DocumentStorage storage;
    private static final Tika TIKA = new Tika();

    public DocumentEntity save(MultipartFile file) {
        if (file.isEmpty()) {
            log.error("No file provided");
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "No file provided"
            );
        }

        String mimeType;
        try {
            mimeType = TIKA.detect(
                file.getInputStream(),
                file.getOriginalFilename()
            );
        } catch (IOException e) {
            log.error("Failed to detect MIME type", e);
            throw new UnsupportedMediaTypeStatusException(
                "Failed to detect MIME type"
            );
        }

        if (!"application/pdf".equalsIgnoreCase(mimeType)) {
            log.error("Unsupported media type: {}", mimeType);
            throw new UnsupportedMediaTypeStatusException(
                "Expected application/pdf but got " + mimeType
            );
        }

        Optional<PdfMetadata> metadata = extractPdfMetadata(file);

        String objectKey = storage.store(file, mimeType.split("/")[1]);

        DocumentEntity.DocumentEntityBuilder builder = DocumentEntity.builder()
            .fileType(mimeType)
            .fileName(file.getOriginalFilename())
            .fileSize(file.getSize())
            .objectKey(objectKey);
        metadata.ifPresent(pdfMetadata ->
            builder
                .docPageCount(pdfMetadata.pageCount())
                .docTitle(pdfMetadata.title())
                .docAuthor(pdfMetadata.author())
                .docCreatedAt(pdfMetadata.createdAt())
                .docUpdatedAt(pdfMetadata.updatedAt())
        );

        DocumentEntity document = builder.build();
        DocumentEntity saved = repository.save(document);

        try {
            byte[] content = file.getBytes();
            DocumentMessage message = DocumentMessage.from(
                saved.getId(),
                content
            );
            publisher.send(message);
        } catch (IOException e) {
            log.error(
                "Failed to read file content for document {}",
                saved.getId(),
                e
            );
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to read file content"
            );
        } catch (DocumentMessagingException e) {
            log.error("Failed to enqueue document {}", saved.getId(), e);
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Failed to forward document to OCR queue"
            );
        }

        return saved;
    }

    private Optional<PdfMetadata> extractPdfMetadata(MultipartFile file) {
        try (
            RandomAccessRead rar = new RandomAccessReadBuffer(
                file.getInputStream()
            )
        ) {
            PDFParser parser = new PDFParser(rar);
            PDDocument pdf = parser.parse();

            try (pdf) {
                int pageCount = pdf.getNumberOfPages();
                log.info("Page count: {}", pageCount);
                PDDocumentInformation info = pdf.getDocumentInformation();
                String title = null;
                String author = null;
                Instant createdAt = null;
                Instant updatedAt = null;
                if (info != null) {
                    title = emptyToNull(info.getTitle());
                    author = emptyToNull(info.getAuthor());
                    log.info("Title: {}", title);
                    log.info("Author: {}", author);
                    log.info("Subject: {}", info.getSubject());
                    log.info("Keywords: {}", info.getKeywords());
                    log.info("Creator: {}", info.getCreator());
                    log.info("Producer: {}", info.getProducer());
                    log.info("Creation Date: {}", info.getCreationDate());
                    log.info(
                        "Modification Date: {}",
                        info.getModificationDate()
                    );
                    log.info("Trapped: {}", info.getTrapped());

                    Calendar creationDate = info.getCreationDate();
                    if (creationDate != null) {
                        createdAt = creationDate.toInstant();
                    }

                    Calendar modificationDate = info.getModificationDate();
                    if (modificationDate != null) {
                        updatedAt = modificationDate.toInstant();
                    }
                }

                PDMetadata metadata = pdf.getDocumentCatalog().getMetadata();
                if (metadata != null) {
                    try {
                        String xmp = new String(
                            metadata.toByteArray(),
                            StandardCharsets.UTF_8
                        );
                        log.info("XMP Metadata:\n{}", xmp);
                    } catch (IOException e) {
                        log.warn("Failed to read XMP metadata", e);
                    }
                }

                return Optional.of(
                    new PdfMetadata(
                        (long) pageCount,
                        title,
                        author,
                        createdAt,
                        updatedAt
                    )
                );
            }
        } catch (IOException e) {
            log.error("Failed to read PDF metadata", e);
        }
        return Optional.empty();
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private record PdfMetadata(
        Long pageCount,
        String title,
        String author,
        Instant createdAt,
        Instant updatedAt
    ) {}

    public List<DocumentDto> findAll() {
        return mapper.toDocumentList(repository.findAll());
    }

    public Optional<DocumentDto> findById(Long id) {
        return repository.findById(id).map(mapper::toDocument);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Document not found"
            );
        }

        repository.deleteById(id);
    }
}
