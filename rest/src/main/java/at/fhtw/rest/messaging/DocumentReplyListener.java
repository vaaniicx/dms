package at.fhtw.rest.messaging;

import at.fhtw.rest.messaging.dto.DocumentReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static at.fhtw.rest.config.RabbitMQConfig.DOCUMENT_PROCESSED_QUEUE;

@Slf4j
@Component
public class DocumentReplyListener {

    @RabbitListener(queues = DOCUMENT_PROCESSED_QUEUE)
    public void handle(DocumentReply reply) {
        log.info("OCR worker finished processing document #{}", reply.documentId());
    }
}
