package at.fhtw.ocr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    public static final String DOCUMENT_EXCHANGE = "document.exchange";
    public static final String DOCUMENT_UPLOAD_QUEUE = "ocr.document.uploaded";
    public static final String DOCUMENT_UPLOAD_ROUTING_KEY = "ocr.document.uploaded";
    public static final String DOCUMENT_PROCESSED_QUEUE = "ocr.document.processed";
    public static final String DOCUMENT_PROCESSED_ROUTING_KEY = "ocr.document.processed";

    @Bean
    public Queue documentUploadQueue() {
        return new Queue(DOCUMENT_UPLOAD_QUEUE, true);
    }

    @Bean
    public Queue documentProcessedQueue() {
        return new Queue(DOCUMENT_PROCESSED_QUEUE, true);
    }

    @Bean
    public DirectExchange documentExchange() {
        return new DirectExchange(DOCUMENT_EXCHANGE, true, false);
    }

    @Bean
    public Binding documentUploadBinding(Queue documentUploadQueue, DirectExchange exchange) {
        return BindingBuilder.bind(documentUploadQueue).to(exchange).with(DOCUMENT_UPLOAD_ROUTING_KEY);
    }

    @Bean
    public Binding documentProcessedBinding(Queue documentProcessedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(documentProcessedQueue).to(exchange).with(DOCUMENT_PROCESSED_ROUTING_KEY);
    }

    @Bean
    public ConnectionFactory connectionFactory(Environment environment) {
        CachingConnectionFactory factory = new CachingConnectionFactory(environment.getProperty("spring.rabbitmq.host", "localhost"));
        factory.setUsername(environment.getProperty("spring.rabbitmq.username", "guest"));
        factory.setPassword(environment.getProperty("spring.rabbitmq.password", "guest"));
        factory.setPort(Integer.parseInt(environment.getProperty("spring.rabbitmq.port", "5672")));
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
}
