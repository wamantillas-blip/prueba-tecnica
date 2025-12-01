package com.banco.pruebatecnica.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_BANK_EVENTS = "bank.events.exchange";
    public static final String QUEUE_CLIENTE_CREADO = "cliente.creado.queue";
    public static final String ROUTING_KEY_CLIENTE_CREADO = "cliente.creado";


    @Bean
    public TopicExchange bankEventsExchange() {
        return new TopicExchange(EXCHANGE_BANK_EVENTS);
    }

    @Bean
    public Queue clienteCreadoQueue() {
        return new Queue(QUEUE_CLIENTE_CREADO, true);
    }

    @Bean
    public Binding clienteCreadoBinding() {
        return BindingBuilder.bind(clienteCreadoQueue())
                .to(bankEventsExchange())
                .with(ROUTING_KEY_CLIENTE_CREADO);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

}