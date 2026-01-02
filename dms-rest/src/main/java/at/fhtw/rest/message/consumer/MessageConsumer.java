package at.fhtw.rest.message.consumer;

import at.fhtw.message.QueueName;
import at.fhtw.message.document.DocumentIndexedMessage;
import at.fhtw.message.document.DocumentScannedMessage;
import at.fhtw.message.document.DocumentSummarizedMessage;
import at.fhtw.rest.core.persistence.entity.DocumentStatus;
import at.fhtw.rest.core.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private final DocumentService documentService;

    @RabbitListener(queues = QueueName.REST_DOCUMENT_SCANNED)
    public void consumeDocumentScanned(final DocumentScannedMessage consumedMessage) {
        documentService.updateDocumentStatus(consumedMessage.documentId(), DocumentStatus.SCANNED);
    }

    @RabbitListener(queues = QueueName.REST_DOCUMENT_INDEXED)
    public void consumeDocumentIndexed(final DocumentIndexedMessage consumedMessage) {
        documentService.updateDocumentStatus(consumedMessage.documentId(), DocumentStatus.INDEXED);
    }

    @RabbitListener(queues = QueueName.REST_DOCUMENT_SUMMARIZED)
    public void consumeDocumentSummarized(final DocumentSummarizedMessage consumedMessage) {
        documentService.updateSummary(consumedMessage.documentId(), consumedMessage.summary());
        documentService.updateDocumentStatus(consumedMessage.documentId(), DocumentStatus.SUMMARIZED);
    }

    @RabbitListener(queues = QueueName.REST_DOCUMENT_STATISTICS)
    public void consumeDocumentStatistics(final DocumentStatisticsMessage consumedMessage) {
        log.info("Received statistics for document {}: accessCount={}, accessor={}",
                consumedMessage.documentId(), consumedMessage.accessCount(), consumedMessage.accessor());

        documentService.updateDocumentAccessHistory(
                consumedMessage.documentId(),
                consumedMessage.accessDate(),
                consumedMessage.accessCount(),
                consumedMessage.accessor()
        );
    }
}
