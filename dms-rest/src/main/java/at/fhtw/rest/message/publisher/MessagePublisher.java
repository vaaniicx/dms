package at.fhtw.rest.message.publisher;

import at.fhtw.message.Exchange;
import at.fhtw.message.RoutingKey;
import at.fhtw.message.document.DocumentUploadedMessage;
import at.fhtw.rest.core.exception.DocumentMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate template;

    public void publishDocumentUploaded(Long documentId, String objectKey) {
        DocumentUploadedMessage documentUploadedMessage = new DocumentUploadedMessage(documentId, objectKey);
        try {
            template.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_UPLOADED, documentUploadedMessage);
        } catch (DocumentMessagingException e) {
            throw new DocumentMessagingException("Failed to enqueue document " + documentId, e);
        }
    }
}
