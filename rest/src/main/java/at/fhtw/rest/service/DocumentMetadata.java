package at.fhtw.rest.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Calendar;
import java.util.Optional;

import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DocumentMetadata {
    public record PdfMetadata(Long pageCount, String title, String author, Instant createdAt, Instant updatedAt) {
    }

    public Optional<PdfMetadata> extract(MultipartFile file) {
        try (RandomAccessRead rar = new RandomAccessReadBuffer(file.getInputStream())) {
            PDFParser parser = new PDFParser(rar);
            PDDocument pdf = parser.parse();

            try (pdf) {
                int pageCount = pdf.getNumberOfPages();
                PDDocumentInformation info = pdf.getDocumentInformation();

                String title = info == null ? null : info.getTitle();
                if (title != null && title.isBlank()) title = null;
                String author = info == null ? null : info.getAuthor();
                if (author != null && author.isBlank()) author = null;

                Instant createdAt = null;
                Instant updatedAt = null;
                if (info != null) {
                    Calendar creationDate = info.getCreationDate();
                    if (creationDate != null) createdAt = creationDate.toInstant();
                    Calendar modificationDate = info.getModificationDate();
                    if (modificationDate != null) updatedAt = modificationDate.toInstant();
                }

                return Optional.of(new PdfMetadata((long) pageCount, title, author, createdAt, updatedAt));
            }
        } catch (IOException e) {
            log.warn("Failed to read PDF metadata", e);
            return Optional.empty();
        }
    }

}
