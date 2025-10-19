package at.fhtw.ocr.messaging;

import at.fhtw.ocr.messaging.dto.DocumentMessage;
import at.fhtw.ocr.ocr.OcrProcessor;
import at.fhtw.ocr.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.nio.file.Files;
import java.nio.file.Path;

import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_EXCHANGE;
import static at.fhtw.ocr.config.RabbitMQConfig.DOCUMENT_PROCESSED_ROUTING_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentMessageListenerTest {

    @Test
    void handle_downloads_runsOcr_andPublishesReply() throws Exception {
        RabbitTemplate template = mock(RabbitTemplate.class);
        StorageService storage = mock(StorageService.class);
        OcrProcessor processor = mock(OcrProcessor.class);

        DocumentMessageListener listener = new DocumentMessageListener(template, storage, processor);

        Path tmp = Files.createTempFile("doc-", ".pdf");
        when(storage.downloadToTempFile("obj.pdf")).thenReturn(tmp);
        when(processor.extractText(tmp)).thenReturn("content");

        listener.handle(new DocumentMessage(42L, "obj.pdf"));

        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);
        verify(template, times(1)).convertAndSend(eq(DOCUMENT_EXCHANGE), eq(DOCUMENT_PROCESSED_ROUTING_KEY), messageCaptor.capture());
        assertThat(messageCaptor.getValue().toString()).contains("42");
        assertThat(messageCaptor.getValue().toString()).contains("content");

        Files.deleteIfExists(tmp);
    }
}

