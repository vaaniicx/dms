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
public class RabbitMQConfig {

    @Bean
    public Declarables declarables() {
        DirectExchange exchange = new DirectExchange(Exchange.DOCUMENT_EXCHANGE, true, false);

        Queue scanQueue = new Queue(QueueName.DOCUMENT_SCANNED, true);
        Binding scanBinding = BindingBuilder
                .bind(scanQueue)
                .to(exchange)
                .with(RoutingKey.DOCUMENT_SCANNED);

        Queue summaryQueue = new Queue(QueueName.DOCUMENT_SUMMARIZED, true);
        Binding summaryBinding = BindingBuilder
                .bind(summaryQueue)
                .to(exchange)
                .with(RoutingKey.DOCUMENT_SUMMARIZED);

        return new Declarables(exchange, scanQueue, scanBinding, summaryQueue, summaryBinding);
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
