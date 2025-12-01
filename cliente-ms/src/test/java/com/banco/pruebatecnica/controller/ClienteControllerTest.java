package com.banco.pruebatecnica.controller;

import com.banco.pruebatecnica.ClienteMsApplication;
import com.banco.pruebatecnica.dto.ClienteRequest;
import com.banco.pruebatecnica.dto.ClienteResponse;
import com.banco.pruebatecnica.exception.ResourceNotFoundException;
import com.banco.pruebatecnica.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ClienteMsApplication.class)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@DisplayName("Pruebas de Integración para ClienteController")
public class ClienteControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ClienteService clienteService;

    private ClienteRequest mockRequest;
    private ClienteResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockRequest = ClienteRequest.builder()
                .nombre("Pedro Perez")
                .identificacion("1122334455")
                .contrasena("pass123")
                .estado(true)
                .build();

        mockResponse = ClienteResponse.builder()
                .clienteId(1L)
                .nombre("Pedro Perez")
                .identificacion("1122334455")
                .estado(true)
                .build();
    }

    @Test
    @DisplayName("T7: POST /clientes - Creación con JSON completo (201)")
    void testCrearClienteConJsonCompleto() {

        String jsonCompleto = """
                {
                    "nombre": "Marianela Montalvo",
                    "genero": "Femenino",
                    "edad": 35,
                    "identificacion": "1234567890",
                    "direccion": "Amazonas y NNUU",
                    "telefono": "097548965",
                    "contrasena": "5678",
                    "estado": true
                }
                """;

        ClienteResponse respuestaEsperada = ClienteResponse.builder()
                .clienteId(1L)
                .nombre("Marianela Montalvo")
                .identificacion("1234567890")
                .direccion("Amazonas y NNUU")
                .telefono("097548965")
                .estado(true)
                .build();

        when(clienteService.crearCliente(any(ClienteRequest.class)))
                .thenReturn(Mono.just(respuestaEsperada));

        webTestClient.post()
                .uri("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonCompleto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponse.class)
                .isEqualTo(respuestaEsperada);

        verify(clienteService, times(1)).crearCliente(any(ClienteRequest.class));
    }


    @Test
    @DisplayName("T2: GET /clientes/{id} - Obtención exitosa (200)")
    void testObtenerClientePorIdExitoso() {
        when(clienteService.obtenerClientePorId(eq(1L))).thenReturn(Mono.just(mockResponse));

        webTestClient.get().uri("/clientes/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponse.class)
                .isEqualTo(mockResponse);

        verify(clienteService, times(1)).obtenerClientePorId(eq(1L));
    }

    @Test
    @DisplayName("T3: GET /clientes/{id} - Recurso no encontrado (404)")
    void testObtenerClientePorIdNoEncontrado() {
        when(clienteService.obtenerClientePorId(eq(999L))).thenReturn(Mono.error(new ResourceNotFoundException("Cliente no encontrado")));

        webTestClient.get().uri("/clientes/{id}", 999L)
                .exchange()
                .expectStatus().isNotFound();

        verify(clienteService, times(1)).obtenerClientePorId(eq(999L));
    }

    @Test
    @DisplayName("T6: PUT /clientes/{id} - Actualización con JSON completo (200)")
    void testActualizarClienteConJsonCompleto() {

        // JSON exacto que deseas enviar
        String jsonCompleto = """
                {
                    "nombre": "Wilmer",
                    "genero": "M",
                    "edad": 33,
                    "identificacion": "11111",
                    "direccion": "NUEVA DIRECCION ACTUALIZADA",
                    "telefono": "0999888777",
                    "contrasena": "claveNueva",
                    "estado": true
                }
                """;

        ClienteResponse respuestaActualizada = ClienteResponse.builder()
                .clienteId(1L)
                .nombre("Wilmer")
                .identificacion("11111")
                .direccion("NUEVA DIRECCION ACTUALIZADA")
                .telefono("0999888777")
                .estado(true)
                .build();

        when(clienteService.actualizarCliente(eq(1L), any(ClienteRequest.class)))
                .thenReturn(Mono.just(respuestaActualizada));

        webTestClient.put()
                .uri("/clientes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonCompleto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponse.class)
                .isEqualTo(respuestaActualizada);

        verify(clienteService, times(1)).actualizarCliente(eq(1L), any(ClienteRequest.class));
    }

    @Test
    @DisplayName("T5: DELETE /clientes/{id} - Eliminación lógica exitosa (204)")
    void testEliminarClienteExitoso() {
        when(clienteService.eliminarCliente(eq(1L))).thenReturn(Mono.empty());

        webTestClient.delete().uri("/clientes/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(clienteService, times(1)).eliminarCliente(eq(1L));
    }
}