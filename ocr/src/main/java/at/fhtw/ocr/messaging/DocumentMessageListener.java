package at.fhtw.ocr.messaging;

import at.fhtw.ocr.messaging.dto.DocumentMessage;
import at.fhtw.ocr.messaging.dto.DocumentReply;
import at.fhtw.ocr.ocr.OcrProcessor;
import at.fhtw.ocr.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_EXCHANGE;
import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_UPLOAD_QUEUE;
import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_PROCESSED_ROUTING_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentMessageListener {
    private final RabbitTemplate template;
    private final StorageService storage;
    private final OcrProcessor ocrProcessor;

    @RabbitListener(queues = DOCUMENT_UPLOAD_QUEUE)
    public void handle(DocumentMessage message) {
        long id = message.documentId();
        log.info("Received document #{} with objectKey {}", id, message.objectKey());

        Path tmpPdf = null;
        try {
            tmpPdf = storage.downloadToTempFile(message.objectKey());
            String text = ocrProcessor.extractText(tmpPdf);
            DocumentReply reply = new DocumentReply(id, text);
            template.convertAndSend(DOCUMENT_EXCHANGE, DOCUMENT_PROCESSED_ROUTING_KEY, reply);
            log.info("Processed document #{} successfully", id);
        } catch (Exception ex) {
            log.error("Failed to process document #{}", id, ex);
        } finally {
            if (tmpPdf != null) {
                try { Files.deleteIfExists(tmpPdf); } catch (Exception ignore) {}
            }
        }
    }
}
