package at.fhtw.rest.messaging;

import at.fhtw.rest.messaging.dto.DocumentReply;
import at.fhtw.rest.persistence.model.DocumentStatus;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static at.fhtw.rest.config.RabbitMqConfig.DOCUMENT_PROCESSED_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentListener {
    private final DocumentRepository repository;

    @RabbitListener(queues = DOCUMENT_PROCESSED_QUEUE)
    @Transactional
    public void handle(DocumentReply reply) {
        long id = reply.documentId();
        repository.findById(id).ifPresentOrElse(document -> {
            document.setDocStatus(DocumentStatus.SCANNED);
            document.setExtractedText(reply.extractedText());
            repository.save(document);
            log.info("Stored OCR text and marked document #{} as SCANNED", id);
        }, () -> log.warn("Received OCR reply for unknown document #{}", id));
    }
}
