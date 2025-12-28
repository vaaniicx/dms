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

    public void publishDocumentUploaded(final DocumentUploadedMessage message) {
        try {
            template.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_UPLOADED, message);
        } catch (Exception e) {
            throw new DocumentMessagingException("Could not publish message.", e);
        }
    }
}
