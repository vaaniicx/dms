package at.fhtw.ocr.message.publisher;

import at.fhtw.message.Exchange;
import at.fhtw.message.RoutingKey;
import at.fhtw.message.document.DocumentProcessedMessage;
import at.fhtw.message.document.DocumentScannedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishDocumentScanned(final DocumentScannedMessage message) {
        log.info("Publishing DocumentScannedMessage");
        try {
            rabbitTemplate.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_SCANNED, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void publishDocumentProcessed(final DocumentProcessedMessage message) {
        log.info("Publishing DocumentProcessedMessage");
        try {
            rabbitTemplate.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_PROCESSED, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
