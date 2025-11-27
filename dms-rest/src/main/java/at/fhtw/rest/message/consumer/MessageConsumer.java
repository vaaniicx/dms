package at.fhtw.rest.message.consumer;

import at.fhtw.message.QueueName;
import at.fhtw.message.document.DocumentSummarizedMessage;
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

    @RabbitListener(queues = QueueName.DOCUMENT_SUMMARIZED)
    public void consumeDocumentSummarized(final DocumentSummarizedMessage consumedMessage) {
        log.info("Consuming DocumentSummarizedMessage");

        documentRepository.findById(consumedMessage.documentId())
                .ifPresentOrElse(document -> {
                    document.setUploaded(true);
                    document.setScanned(true);
                    document.setSummarized(true);
                    document.setSummary(consumedMessage.summary());
                    documentRepository.save(document);
                    log.info("Stored summary for document {}", document.getId());
                }, () -> {
                    log.warn("Received unknown document: {}", consumedMessage.documentId());
                });
    }
}
