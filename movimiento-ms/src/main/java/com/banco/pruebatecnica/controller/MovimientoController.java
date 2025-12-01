package com.banco.pruebatecnica.controller;

import com.banco.pruebatecnica.dto.MovimientoRequest;
import com.banco.pruebatecnica.dto.MovimientoResponse;
import com.banco.pruebatecnica.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovimientoResponse> registrarMovimiento(@Valid @RequestBody Mono<MovimientoRequest> request) {
        return request.flatMap(movimientoService::registrarMovimiento);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovimientoResponse> obtenerMovimientosPorCuenta(@RequestParam String numeroCuenta) {
        return movimientoService.obtenerMovimientosPorCuenta(numeroCuenta);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Mono<MovimientoResponse> actualizarMovimiento(@PathVariable Long id,
                                                         @Valid @RequestBody Mono<MovimientoRequest> request) {
        return Mono.error(new UnsupportedOperationException("La actualización directa de movimientos no está permitida."));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Mono<Void> eliminarMovimiento(@PathVariable Long id) {
        return Mono.error(new UnsupportedOperationException("La eliminación física de movimientos no está permitida."));
    }
}