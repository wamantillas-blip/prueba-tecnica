package com.banco.pruebatecnica.controller;

import com.banco.pruebatecnica.dto.EstadoCuentaResponse;
import com.banco.pruebatecnica.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<EstadoCuentaResponse> obtenerEstadoCuenta(
            @RequestParam(name = "cliente") Long clienteId,
            @RequestParam(name = "fechaInicio") String fechaInicioStr,
            @RequestParam(name = "fechaFin") String fechaFinStr) {

        try {
            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr);
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr);

            return reporteService.generarEstadoCuenta(clienteId, fechaInicio, fechaFin);

        } catch (DateTimeParseException e) {
            return Mono.error(new IllegalArgumentException("Formato de fecha inválido. Use el formato ISO 8601 (ej: 2024-10-20T00:00:00)."));
        }
    }
}