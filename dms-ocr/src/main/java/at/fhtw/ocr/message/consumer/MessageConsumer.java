package at.fhtw.ocr.message.consumer;

import at.fhtw.message.QueueName;
import at.fhtw.message.document.DocumentUploadedMessage;
import at.fhtw.ocr.message.publisher.MessagePublisher;
import at.fhtw.ocr.service.ElasticsearchService;
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

    private final ElasticsearchService elasticsearchService;

    @RabbitListener(queues = QueueName.OCR_DOCUMENT_UPLOADED)
    public void consumeDocumentUploaded(final DocumentUploadedMessage consumedMessage) {
        String scannedText = ocrService.scanDocument(consumedMessage.objectKey());
        messagePublisher.publishDocumentScanned(consumedMessage.documentId(), scannedText);

        elasticsearchService.indexDocument(consumedMessage.documentId(), scannedText);
        messagePublisher.publishDocumentIndexed(consumedMessage.documentId());
    }
}
