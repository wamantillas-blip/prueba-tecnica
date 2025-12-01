package com.banco.pruebatecnica.controller;

import com.banco.pruebatecnica.dto.CuentaRequest;
import com.banco.pruebatecnica.dto.CuentaResponse;
import com.banco.pruebatecnica.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CuentaResponse> crearCuenta(@Valid @RequestBody Mono<CuentaRequest> request) {
        return request.flatMap(cuentaService::crearCuenta);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CuentaResponse> obtenerCuentaPorId(@PathVariable Long id) {
        return cuentaService.obtenerCuentaPorId(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<CuentaResponse> obtenerTodasLasCuentas() {
        return cuentaService.obtenerTodasLasCuentas();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CuentaResponse> actualizarCuenta(@PathVariable Long id,
                                                 @Valid @RequestBody Mono<CuentaRequest> request) {
        return request.flatMap(r -> cuentaService.actualizarCuenta(id, r));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Mono<Void> eliminarCuenta(@PathVariable Long id) {
        return Mono.error(new UnsupportedOperationException("La eliminación directa de cuentas no está permitida. Use PATCH para desactivar."));
    }
}