package at.fthw.ai.message.consumer;

import at.fhtw.message.QueueName;
import at.fhtw.message.document.DocumentScannedMessage;
import at.fthw.ai.message.publisher.MessagePublisher;
import at.fthw.ai.service.SummarizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private final MessagePublisher messagePublisher;

    private final SummarizerService summarizerService;

    @RabbitListener(queues = QueueName.GENAI_DOCUMENT_SCANNED)
    public void consumeDocumentScanned(final DocumentScannedMessage consumedMessage) {
        String summary = summarizerService.summarize(consumedMessage.scannedText());
        messagePublisher.publishDocumentSummarized(consumedMessage.documentId(), summary);
    }
}
