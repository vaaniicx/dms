import at.fhtw.message.document.DocumentIndexedMessage;
import at.fhtw.message.document.DocumentScannedMessage;
import at.fhtw.message.document.DocumentSummarizedMessage;
import at.fhtw.rest.core.persistence.entity.DocumentStatus;
import at.fhtw.rest.core.persistence.entity.DocumentStatusHistory;
import at.fhtw.rest.core.service.DocumentService;
import at.fhtw.rest.message.consumer.MessageConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

    @InjectMocks
    private MessageConsumer underTest;

    @Mock
    private DocumentService documentService;

    @Test
    void shouldUpdateStatusToScannedWhenConsumingDocumentScannedMessage() {
        DocumentScannedMessage message = new DocumentScannedMessage(1L, "The scanned text.");

        underTest.consumeDocumentScanned(message);

        ArgumentCaptor<DocumentStatusHistory> captor = ArgumentCaptor.forClass(DocumentStatusHistory.class);
        verify(documentService).updateDocumentStatus(eq(1L), captor.capture());

        assertThat(DocumentStatus.SCANNED).isEqualTo(captor.getValue().getStatus());
    }

    @Test
    void shouldUpdateStatusToIndexedWhenConsumingDocumentIndexedMessage() {
        DocumentIndexedMessage message = new DocumentIndexedMessage(1L);

        underTest.consumeDocumentIndexed(message);

        ArgumentCaptor<DocumentStatusHistory> captor = ArgumentCaptor.forClass(DocumentStatusHistory.class);
        verify(documentService).updateDocumentStatus(eq(1L), captor.capture());

        assertThat(DocumentStatus.INDEXED).isEqualTo(captor.getValue().getStatus());
    }

    @Test
    void shouldUpdateStatusToSummarizedWhenConsumingDocumentSummarizedMessage() {
        DocumentSummarizedMessage message = new DocumentSummarizedMessage(1L, "The summary.");

        underTest.consumeDocumentSummarized(message);

        ArgumentCaptor<DocumentStatusHistory> captor = ArgumentCaptor.forClass(DocumentStatusHistory.class);
        verify(documentService).updateDocumentStatus(eq(1L), captor.capture());

        assertThat(DocumentStatus.SUMMARIZED).isEqualTo(captor.getValue().getStatus());
    }
}
