package at.fhtw.rest.message.consumer;

import at.fhtw.message.QueueName;
import at.fhtw.message.document.DocumentProcessedMessage;
import at.fhtw.rest.persistence.model.DocumentStatus;
import at.fhtw.rest.persistence.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private final DocumentRepository documentRepository;

    @RabbitListener(queues = QueueName.DOCUMENT_PROCESSED)
    public void consumeDocumentUploaded(final DocumentProcessedMessage consumedMessage) {
        log.info("Consuming DocumentProcessedMessage");

        documentRepository.findById(consumedMessage.documentId())
                .ifPresentOrElse(document -> {
                    document.setDocStatus(DocumentStatus.SCANNED);
                    document.setExtractedText(consumedMessage.summarizedText()); // todo: rename to summary
                    log.info("Summarized Text: {}", document.getExtractedText());
                }, () -> {
                    log.warn("Received unknown document: {}", consumedMessage.documentId());
                });
    }
}
