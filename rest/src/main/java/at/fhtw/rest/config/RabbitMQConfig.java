package at.fhtw.rest.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String DOCUMENT_EXCHANGE = "document.exchange";
    public static final String DOCUMENT_UPLOAD_QUEUE = "ocr.document.uploaded";
    public static final String DOCUMENT_UPLOAD_ROUTING_KEY = "ocr.document.uploaded";
    public static final String DOCUMENT_PROCESSED_QUEUE = "ocr.document.processed";
    public static final String DOCUMENT_PROCESSED_ROUTING_KEY = "ocr.document.processed";

    @Bean
    public Declarables documentDeclarables() {
        DirectExchange exchange = new DirectExchange(DOCUMENT_EXCHANGE, true, false);

        Queue uploadQueue = new Queue(DOCUMENT_UPLOAD_QUEUE, true);
        Queue processedQueue = new Queue(DOCUMENT_PROCESSED_QUEUE, true);

        Binding uploadBinding = BindingBuilder
                .bind(uploadQueue)
                .to(exchange)
                .with(DOCUMENT_UPLOAD_ROUTING_KEY);

        Binding processedBinding = BindingBuilder
                .bind(processedQueue)
                .to(exchange)
                .with(DOCUMENT_PROCESSED_ROUTING_KEY);

        return new Declarables(exchange, uploadQueue, processedQueue, uploadBinding, processedBinding);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        return new RabbitTemplate(factory);
    }
}