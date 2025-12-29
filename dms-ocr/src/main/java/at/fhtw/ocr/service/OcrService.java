package at.fhtw.ocr.service;

import at.fhtw.ocr.persistence.storage.DocumentStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.StringJoiner;

@Slf4j
@Service
@AllArgsConstructor
public class OcrService {

    private final DocumentStorage storage;

    public String scanDocument(String objectKey) {
        ensureValidObjectKey(objectKey);
        return extractTextFromDocument(objectKey);
    }

    private String extractTextFromDocument(String objectKey) {
        StringJoiner joiner = new StringJoiner("\n\n");

        try (InputStream is = storage.load(objectKey)) {
            byte[] bytes = is.readAllBytes();

            try (PDDocument document = Loader.loadPDF(bytes)) {
                PDFRenderer renderer = new PDFRenderer(document);
                ITesseract tesseract = new Tesseract();

                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    joiner.add(tesseract.doOCR(renderer.renderImageWithDPI(i, 300)));
                }
            }
        } catch (Exception ex) {
            log.error("Failed to perform OCR on object {}", objectKey, ex);
            throw new RuntimeException("Failed to perform OCR", ex);
        }
        return joiner.toString();
    }

    private void ensureValidObjectKey(String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) {
            throw new IllegalStateException("Received uploaded document which has no object key.");
        }
    }
}
