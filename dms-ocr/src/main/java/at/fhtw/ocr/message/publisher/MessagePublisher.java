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

    public void publishDocumentScanned(Long documentId, String scannedText) {
        DocumentScannedMessage documentScannedMessage = new DocumentScannedMessage(documentId, scannedText);
        try {
            rabbitTemplate.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_SCANNED, documentScannedMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void publishDocumentIndexed(Long documentId) {
        DocumentIndexedMessage documentIndexedMessage = new DocumentIndexedMessage(documentId);
        try {
            rabbitTemplate.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_INDEXED, documentIndexedMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
