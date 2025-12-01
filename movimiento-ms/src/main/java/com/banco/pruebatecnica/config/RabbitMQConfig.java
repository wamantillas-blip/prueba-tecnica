package com.banco.pruebatecnica.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_BANK_EVENTS = "bank.events.exchange";
    public static final String QUEUE_CLIENTE_CREADO_MS2 = "cuenta.creacion.queue";
    public static final String ROUTING_KEY_CLIENTE_CREADO = "cliente.creado";

    @Bean
    public TopicExchange bankEventsExchange() {
        return new TopicExchange(EXCHANGE_BANK_EVENTS);
    }

    @Bean
    public Queue cuentaCreacionQueue() {
        return new Queue(QUEUE_CLIENTE_CREADO_MS2, true);
    }

    @Bean
    public Binding cuentaCreacionBinding() {
        return BindingBuilder.bind(cuentaCreacionQueue())
                .to(bankEventsExchange())
                .with(ROUTING_KEY_CLIENTE_CREADO);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}