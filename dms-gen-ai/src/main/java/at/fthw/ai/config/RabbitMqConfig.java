package at.fthw.ai.config;

import at.fhtw.message.Exchange;
import at.fhtw.message.QueueName;
import at.fhtw.message.RoutingKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMqConfig {

    @Bean
    public Declarables declarables() {
        DirectExchange exchange = new DirectExchange(Exchange.DOCUMENT_EXCHANGE, true, false);

        Queue ocrScanQueue = new Queue(QueueName.GENAI_DOCUMENT_SCANNED, true);
        Binding ocrScanBinding = BindingBuilder
                .bind(ocrScanQueue)
                .to(exchange)
                .with(RoutingKey.DOCUMENT_SCANNED);

        Queue restSummaryQueue = new Queue(QueueName.REST_DOCUMENT_SUMMARIZED, true);
        Binding restSummaryBinding = BindingBuilder
                .bind(restSummaryQueue)
                .to(exchange)
                .with(RoutingKey.DOCUMENT_SUMMARIZED);

        return new Declarables(exchange,
                ocrScanQueue, ocrScanBinding,
                restSummaryQueue, restSummaryBinding);
    }

    @Bean
    public MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
