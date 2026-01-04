package at.fhtw.batch.message.publisher;

import at.fhtw.batch.mapper.AccessLogMapper;
import at.fhtw.message.Exchange;
import at.fhtw.message.RoutingKey;
import at.fhtw.message.document.DocumentAccessedMessage;
import generated.FullAccessLogReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate template;

    private final AccessLogMapper accessLogMapper;

    public void publishDocumentAccessed(LocalDate accessDate, FullAccessLogReport.AccessLog accessLog) {
        DocumentAccessedMessage documentAccessedMessage = accessLogMapper.mapToDocumentAccessedMessage(accessLog);
        documentAccessedMessage.setAccessDate(accessDate);
        try {
            log.info("Publishing document accessed message");
            template.convertAndSend(Exchange.DOCUMENT_EXCHANGE, RoutingKey.DOCUMENT_ACCESSED, documentAccessedMessage);
        } catch (AmqpException e) {
            log.error("Failed to publish document accessed message", e);
            // todo: handle error
        }
    }
}