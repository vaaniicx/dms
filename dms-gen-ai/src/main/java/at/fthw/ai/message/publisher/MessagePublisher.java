package at.fthw.ai.message.publisher;

import at.fhtw.message.Exchange;
import at.fhtw.message.RoutingKey;
import at.fhtw.message.document.DocumentSummarizedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishDocumentSummarized(final DocumentSummarizedMessage message) {
        try {
            rabbitTemplate.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_SUMMARIZED, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
