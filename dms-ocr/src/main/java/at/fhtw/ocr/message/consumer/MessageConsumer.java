package at.fhtw.ocr.message.consumer;

import at.fhtw.message.QueueName;
import at.fhtw.message.document.DocumentScannedMessage;
import at.fhtw.message.document.DocumentUploadedMessage;
import at.fhtw.ocr.message.publisher.MessagePublisher;
import at.fhtw.ocr.service.OcrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private final MessagePublisher messagePublisher;

    private final OcrService ocrService;

    @RabbitListener(queues = QueueName.DOCUMENT_UPLOADED)
    public void consumeDocumentUploaded(final DocumentUploadedMessage consumedMessage) {
        log.info("Consuming DocumentUploadedMessage");
        if (consumedMessage.objectKey() == null) {
            throw new IllegalStateException("Received uploaded document which has no object key.");
        }

        String extractedText = ocrService.extractText(consumedMessage.objectKey());
        log.info("Extracted text: {}", extractedText);

        DocumentScannedMessage documentScannedMessage = new DocumentScannedMessage(consumedMessage.documentId(), extractedText);
        try {
            messagePublisher.publishDocumentScanned(documentScannedMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
