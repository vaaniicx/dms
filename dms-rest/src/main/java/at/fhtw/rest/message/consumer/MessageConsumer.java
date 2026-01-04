package at.fhtw.rest.message.consumer;

import at.fhtw.message.QueueName;
import at.fhtw.message.document.DocumentAccessedMessage;
import at.fhtw.message.document.DocumentIndexedMessage;
import at.fhtw.message.document.DocumentScannedMessage;
import at.fhtw.message.document.DocumentSummarizedMessage;
import at.fhtw.rest.core.exception.DocumentNotFoundException;
import at.fhtw.rest.core.persistence.entity.DocumentAccessHistory;
import at.fhtw.rest.core.persistence.entity.DocumentStatus;
import at.fhtw.rest.core.persistence.entity.DocumentStatusHistory;
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

    private static DocumentAccessHistory buildDocumentAccessHistory(DocumentAccessedMessage consumedMessage) {
        return DocumentAccessHistory.builder()
                .documentId(consumedMessage.getDocumentId())
                .accessor(consumedMessage.getAccessor())
                .accessDate(consumedMessage.getAccessDate())
                .accessCount(consumedMessage.getAccessCount())
                .build();
    }

    @RabbitListener(queues = QueueName.REST_DOCUMENT_SCANNED)
    public void consumeDocumentScanned(final DocumentScannedMessage consumedMessage) {
        DocumentStatusHistory statusHistory = new DocumentStatusHistory(DocumentStatus.SCANNED);
        updateDocumentStatus(consumedMessage.documentId(), statusHistory);
    }

    @RabbitListener(queues = QueueName.REST_DOCUMENT_INDEXED)
    public void consumeDocumentIndexed(final DocumentIndexedMessage consumedMessage) {
        DocumentStatusHistory statusHistory = new DocumentStatusHistory(DocumentStatus.INDEXED);
        updateDocumentStatus(consumedMessage.documentId(), statusHistory);
    }

    @RabbitListener(queues = QueueName.REST_DOCUMENT_SUMMARIZED)
    public void consumeDocumentSummarized(final DocumentSummarizedMessage consumedMessage) {
        documentService.updateSummary(consumedMessage.documentId(), consumedMessage.summary());

        DocumentStatusHistory statusHistory = new DocumentStatusHistory(DocumentStatus.SUMMARIZED);
        updateDocumentStatus(consumedMessage.documentId(), statusHistory);
    }

    @RabbitListener(queues = QueueName.REST_DOCUMENT_ACCESSED)
    public void consumeDocumentAccessed(final DocumentAccessedMessage consumedMessage) {
        log.info("Received a document accessed message");
        DocumentAccessHistory accessHistory = buildDocumentAccessHistory(consumedMessage);
        try {
            documentService.updateDocumentAccessHistory(consumedMessage.getDocumentId(), accessHistory);
        } catch (DocumentNotFoundException e) {
            log.error("Could not update access history for document with id {}: {}",
                    consumedMessage.getDocumentId(), e.getMessage());
        }
    }

    private void updateDocumentStatus(Long documentId, DocumentStatusHistory statusHistory) {
        try {
            documentService.updateDocumentStatus(documentId, statusHistory);
        } catch (DocumentNotFoundException e) {
            log.error("Could not update status for document with id {}: {}", documentId, e.getMessage());
        }
    }
}
