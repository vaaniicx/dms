package at.fhtw.ocr.messaging;

import at.fhtw.ocr.messaging.dto.DocumentMessage;
import at.fhtw.ocr.messaging.dto.DocumentReply;
import at.fhtw.ocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_EXCHANGE;
import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_UPLOAD_QUEUE;
import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_PROCESSED_ROUTING_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentListener {
    private final RabbitTemplate rabbit;
    private final OcrService ocr;

    @RabbitListener(queues = DOCUMENT_UPLOAD_QUEUE, concurrency = "2-8")
    public void handle(DocumentMessage message) {
        log.info("Received {}", message);
        try {
            String text = ocr.extractText(message.objectKey());
            DocumentReply reply = new DocumentReply(message.documentId(), text);
            rabbit.convertAndSend(DOCUMENT_EXCHANGE, DOCUMENT_PROCESSED_ROUTING_KEY, reply);
        } catch (Exception ex) {
            log.error("Failed to process {}", message, ex);
            throw new RuntimeException(ex);
        }
    }
}
