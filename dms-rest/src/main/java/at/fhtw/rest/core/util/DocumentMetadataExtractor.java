package at.fhtw.rest.core.util;

import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;

public final class DocumentMetadataExtractor {

    private DocumentMetadataExtractor() {
    }

    public static DocumentMetadata extract(MultipartFile file) {
        if (file == null) {
            return new DocumentMetadata();
        }

        try (RandomAccessRead read = new RandomAccessReadBuffer(file.getInputStream())) {
            PDFParser pdfParser = new PDFParser(read);

            DocumentMetadata metadata = new DocumentMetadata();
            try (PDDocument pdDocument = pdfParser.parse()) {
                fillMetadataFromDocument(metadata, pdDocument);
            }
            return metadata;
        } catch (IOException ex) {
            return new DocumentMetadata();
        }
    }

    private static void fillMetadataFromDocument(DocumentMetadata metadata, PDDocument pdDocument) {
        PDDocumentInformation documentInformation = pdDocument.getDocumentInformation();

        metadata.setTitle(documentInformation.getTitle());
        metadata.setAuthor(documentInformation.getAuthor());
        metadata.setPageCount(pdDocument.getNumberOfPages());
        metadata.setCreationDate(getLocalDateTimeOfCalendar(documentInformation.getCreationDate()));
        metadata.setModificationDate(getLocalDateTimeOfCalendar(documentInformation.getModificationDate()));
    }

    private static LocalDateTime getLocalDateTimeOfCalendar(Calendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
    }
}
