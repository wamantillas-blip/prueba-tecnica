package com.banco.pruebatecnica.messaging;

import com.banco.pruebatecnica.config.RabbitMQConfig;
import com.banco.pruebatecnica.dto.ClienteEvent;
import com.banco.pruebatecnica.entity.Cuenta;
import com.banco.pruebatecnica.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClienteListener {

    private final CuentaRepository cuentaRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CLIENTE_CREADO_MS2)
    public void handleClienteEvent(ClienteEvent event) {
        System.out.println("MS2: Evento de Cliente Recibido: " + event.getTipoEvento() + " | ID: " + event.getClienteId());

        if ("CREADO".equals(event.getTipoEvento())) {
            String newAccountNumber = UUID.randomUUID().toString().substring(0, 8);

            Cuenta nuevaCuenta = Cuenta.builder()
                    .numeroCuenta(newAccountNumber)
                    .tipo("Ahorros")
                    .saldoInicial(BigDecimal.ZERO)
                    .saldo(BigDecimal.ZERO)
                    .estado(true)
                    .clienteId(event.getClienteId())
                    .build();

            Mono<Cuenta> saveOperation = cuentaRepository.save(nuevaCuenta)
                    .doOnSuccess(savedAccount ->
                            System.out.println("MS2: Cuenta de Ahorros inicial creada para Cliente ID " + event.getClienteId() + ". Cuenta N°: " + savedAccount.getNumeroCuenta())
                    )
                    .doOnError(e ->
                            System.err.println("MS2: Error al crear cuenta inicial: " + e.getMessage())
                    );

            saveOperation.block();

        } else if ("ELIMINADO_LOGICO".equals(event.getTipoEvento())) {
            // Lógica para desactivar todas las cuentas asociadas al Cliente
            Flux<Cuenta> updateOperation = cuentaRepository.findAllByClienteId(event.getClienteId())
                    .flatMap(cuenta -> {
                        cuenta.setEstado(false);
                        return cuentaRepository.save(cuenta);
                    });

            updateOperation
                    .doOnComplete(() ->
                            System.out.println("MS2: Desactivadas todas las cuentas para Cliente ID: " + event.getClienteId())
                    )
                    .subscribe();
        }
    }
}