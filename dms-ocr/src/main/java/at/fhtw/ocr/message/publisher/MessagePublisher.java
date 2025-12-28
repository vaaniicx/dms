package at.fhtw.ocr.message.publisher;

import at.fhtw.message.Exchange;
import at.fhtw.message.RoutingKey;
import at.fhtw.message.document.DocumentIndexedMessage;
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
        try {
            rabbitTemplate.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_SCANNED, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void publishDocumentIndexed(final DocumentIndexedMessage message) {
        try {
            rabbitTemplate.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_INDEXED, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
