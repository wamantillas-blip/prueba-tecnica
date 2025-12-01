package com.banco.pruebatecnica.service;

import com.banco.pruebatecnica.dto.ClienteRequest;
import com.banco.pruebatecnica.dto.ClienteResponse;
import com.banco.pruebatecnica.entity.Cliente;
import com.banco.pruebatecnica.mapper.ClienteMapper;
import com.banco.pruebatecnica.messaging.ClienteProducer;
import com.banco.pruebatecnica.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para ClienteService (Reactivo)")
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ClienteMapper clienteMapper;
    @Mock
    private ClienteProducer clienteProducer;

    private ClienteRequest request;
    private Cliente cliente;
    private ClienteResponse response;

    @BeforeEach
    void setUp() {
        request = ClienteRequest.builder()
                .nombre("Juan Osorio")
                .identificacion("0987654321")
                .contrasena("1234")
                .estado(true)
                .edad(30)
                .build();

        cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan Osorio")
                .identificacion("0987654321")
                .contrasena("1234_cifrada")
                .estado(true)
                .edad(30)
                .build();

        response = ClienteResponse.builder()
                .clienteId(1L)
                .nombre("Juan Osorio")
                .identificacion("0987654321")
                .estado(true)
                .build();
    }

    @Test
    @DisplayName("T1: Debe crear un cliente nuevo exitosamente y notificar a RabbitMQ")
    void debeCrearClienteExitosamente() {
        when(clienteRepository.findByIdentificacion(request.getIdentificacion())).thenReturn(Mono.empty());
        when(clienteMapper.toEntity(any(ClienteRequest.class))).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(Mono.just(cliente));
        when(clienteMapper.toResponse(any(Cliente.class))).thenReturn(response);

        StepVerifier.create(clienteService.crearCliente(request))
                .expectNext(response)
                .verifyComplete();

        verify(clienteProducer, times(1)).sendClienteEvent(any());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("T2: Debe fallar al crear si la identificación ya está registrada")
    void debeFallarSiIdentificacionExiste() {
        when(clienteRepository.findByIdentificacion(request.getIdentificacion()))
                .thenReturn(Mono.just(cliente));

        StepVerifier.create(clienteService.crearCliente(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().contains("Ya existe un cliente con esta identificación"))
                .verify();

        verify(clienteRepository, never()).save(any());
        verify(clienteProducer, never()).sendClienteEvent(any());
    }

    @Test
    @DisplayName("T3: Debe obtener un cliente por ID existente")
    void debeObtenerClientePorIdExitosamente() {
        when(clienteRepository.findById(1L)).thenReturn(Mono.just(cliente));
        when(clienteMapper.toResponse(any(Cliente.class))).thenReturn(response);

        StepVerifier.create(clienteService.obtenerClientePorId(1L))
                .expectNext(response)
                .verifyComplete();
    }
}
