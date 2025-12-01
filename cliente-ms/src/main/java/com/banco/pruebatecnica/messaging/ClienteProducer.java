package com.banco.pruebatecnica.messaging;

import com.banco.pruebatecnica.config.RabbitMQConfig;
import com.banco.pruebatecnica.dto.ClienteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendClienteEvent(ClienteEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_BANK_EVENTS,
                RabbitMQConfig.ROUTING_KEY_CLIENTE_CREADO,
                event
        );
        System.out.println("Evento de Cliente enviado: " + event.getTipoEvento() + " para ID: " + event.getClienteId());
    }
}