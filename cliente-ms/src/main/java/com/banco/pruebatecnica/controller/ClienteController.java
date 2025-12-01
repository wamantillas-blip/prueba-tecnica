package com.banco.pruebatecnica.controller;

import com.banco.pruebatecnica.dto.ClienteRequest;
import com.banco.pruebatecnica.dto.ClienteResponse;
import com.banco.pruebatecnica.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClienteResponse> crearCliente(@Valid @RequestBody Mono<ClienteRequest> request) {
        return request.flatMap(clienteService::crearCliente);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ClienteResponse> obtenerClientePorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<ClienteResponse> obtenerTodosLosClientes() {
        return clienteService.obtenerTodosLosClientes();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ClienteResponse> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {

        return clienteService.actualizarCliente(id, request);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarCliente(@PathVariable Long id) {
        return clienteService.eliminarCliente(id);
    }
}