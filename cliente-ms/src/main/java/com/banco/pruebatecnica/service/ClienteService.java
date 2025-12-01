package com.banco.pruebatecnica.service;

import com.banco.pruebatecnica.dto.ClienteEvent;
import com.banco.pruebatecnica.dto.ClienteRequest;
import com.banco.pruebatecnica.dto.ClienteResponse;
import com.banco.pruebatecnica.entity.Cliente;
import com.banco.pruebatecnica.exception.ResourceNotFoundException;
import com.banco.pruebatecnica.mapper.ClienteMapper;
import com.banco.pruebatecnica.messaging.ClienteProducer;
import com.banco.pruebatecnica.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteProducer clienteProducer;

    public Mono<ClienteResponse> crearCliente(ClienteRequest request) {
        return clienteRepository.findByIdentificacion(request.getIdentificacion())
                .flatMap(existingClient ->
                        Mono.error(new IllegalArgumentException("Ya existe un cliente con esta identificación: " + request.getIdentificacion()))
                )
                .switchIfEmpty(Mono.defer(() -> {
                    Cliente cliente = clienteMapper.toEntity(request);
                    cliente.setEstado(true);

                    return clienteRepository.save(cliente)
                            .flatMap(savedCliente -> {
                                ClienteEvent event = ClienteEvent.builder()
                                        .clienteId(savedCliente.getId())
                                        .identificacion(savedCliente.getIdentificacion())
                                        .nombre(savedCliente.getNombre())
                                        .estado(savedCliente.getEstado())
                                        .tipoEvento("CREADO")
                                        .build();

                                clienteProducer.sendClienteEvent(event);

                                return Mono.just(clienteMapper.toResponse(savedCliente));
                            });
                }))
                .cast(ClienteResponse.class);
    }


    public Mono<ClienteResponse> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente no encontrado con ID: " + id)))
                .map(clienteMapper::toResponse);
    }

    public Flux<ClienteResponse> obtenerTodosLosClientes() {
        return clienteRepository.findAll()
                .map(clienteMapper::toResponse);
    }

    public Mono<ClienteResponse> actualizarCliente(Long id, ClienteRequest request) {
        return clienteRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente no encontrado con ID: " + id)))
                .flatMap(cliente -> {
                    clienteMapper.updateEntityFromRequest(request, cliente);
                    return clienteRepository.save(cliente);
                })
                .map(updatedCliente -> {
                    ClienteEvent event = ClienteEvent.builder()
                            .clienteId(updatedCliente.getId())
                            .identificacion(updatedCliente.getIdentificacion())
                            .nombre(updatedCliente.getNombre())
                            .estado(updatedCliente.getEstado())
                            .tipoEvento("ACTUALIZADO")
                            .build();
                    clienteProducer.sendClienteEvent(event);
                    return clienteMapper.toResponse(updatedCliente);
                });
    }

    public Mono<Void> eliminarCliente(Long id) {
        return clienteRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cliente no encontrado con ID: " + id)))
                .flatMap(cliente -> {
                    if (cliente.getEstado()) {
                        cliente.setEstado(false);

                        return clienteRepository.save(cliente)
                                .doOnSuccess(deactivatedCliente -> {
                                    ClienteEvent event = ClienteEvent.builder()
                                            .clienteId(deactivatedCliente.getId())
                                            .estado(false)
                                            .tipoEvento("ELIMINADO_LOGICO")
                                            .build();
                                    clienteProducer.sendClienteEvent(event);
                                })
                                .then();
                    }
                    return Mono.empty();
                });
    }
}