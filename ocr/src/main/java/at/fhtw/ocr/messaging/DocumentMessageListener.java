package at.fhtw.ocr.messaging;

import at.fhtw.ocr.messaging.dto.DocumentMessage;
import at.fhtw.ocr.messaging.dto.DocumentReply;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_EXCHANGE;
import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_UPLOAD_QUEUE;
import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_PROCESSED_ROUTING_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentMessageListener {
    private final RabbitTemplate template;

    @RabbitListener(queues = DOCUMENT_UPLOAD_QUEUE)
    public void handle(DocumentMessage message) {
        log.info("Received document #{}", message.documentId());

        try {
            DocumentReply reply = new DocumentReply(message.documentId(), "");

            Thread.sleep(ThreadLocalRandom.current().nextInt(2, 6) * 1000L);

            template.convertAndSend(DOCUMENT_EXCHANGE, DOCUMENT_PROCESSED_ROUTING_KEY, reply);
        } catch (Exception ex) {
            log.error("Failed to process document #{}", message.documentId(), ex);
        }
    }
}
