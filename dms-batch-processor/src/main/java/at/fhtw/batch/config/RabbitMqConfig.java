package at.fhtw.batch.config;

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

        Queue restAccessQueue = new Queue(QueueName.REST_DOCUMENT_ACCESSED, true);
        Binding restAccessBinding = BindingBuilder
                .bind(restAccessQueue)
                .to(exchange)
                .with(RoutingKey.DOCUMENT_ACCESSED);

        return new Declarables(exchange, restAccessQueue, restAccessBinding);
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
