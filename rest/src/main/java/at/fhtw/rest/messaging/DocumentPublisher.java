package at.fhtw.rest.messaging;

import static at.fhtw.rest.config.RabbitMqConfig.DOCUMENT_EXCHANGE;
import static at.fhtw.rest.config.RabbitMqConfig.DOCUMENT_UPLOAD_ROUTING_KEY;

import at.fhtw.rest.exception.DocumentMessagingException;
import at.fhtw.rest.messaging.dto.DocumentMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void send(DocumentMessage documentMessage) {
        try {
            rabbitTemplate.convertAndSend(DOCUMENT_EXCHANGE, DOCUMENT_UPLOAD_ROUTING_KEY, documentMessage);
            log.info("Sent document #{} to OCR queue", documentMessage.documentId());
        } catch (Exception ex) {
            log.error("Failed to send document #{} to OCR queue", documentMessage.documentId(), ex);
            throw new DocumentMessagingException("Failed to send message", ex);
        }
    }
}
