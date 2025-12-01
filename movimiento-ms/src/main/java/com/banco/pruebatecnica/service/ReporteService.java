package com.banco.pruebatecnica.service;


import com.banco.pruebatecnica.dto.EstadoCuentaResponse;
import com.banco.pruebatecnica.entity.Cuenta;
import com.banco.pruebatecnica.exception.ResourceNotFoundException;
import com.banco.pruebatecnica.repository.CuentaRepository;
import com.banco.pruebatecnica.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final WebClient.Builder webClientBuilder;

    @Data
    private static class ClienteMsResponse {
        private Long clienteId;
        private String nombre;
    }


    public Mono<EstadoCuentaResponse> generarEstadoCuenta(
            Long clienteId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        Mono<ClienteMsResponse> clienteMono = webClientBuilder.build()
                .get()
                .uri("http://localhost:8081/clientes/{id}", clienteId)
                .retrieve()
                .bodyToMono(ClienteMsResponse.class)
                .onErrorResume(e -> Mono.error(new ResourceNotFoundException("Cliente no encontrado o Microservicio Cliente no disponible.")));

        Flux<Cuenta> cuentasFlux = cuentaRepository.findAllByClienteId(clienteId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("No se encontraron cuentas para el cliente ID: " + clienteId)));

        Mono<List<EstadoCuentaResponse.CuentaReporteDto>> cuentasReporteMono = cuentasFlux
                .flatMap(cuenta -> {
                    return movimientoRepository.findByNumeroCuentaAndFechaBetween(
                                    cuenta.getNumeroCuenta(),
                                    fechaInicio,
                                    fechaFin)
                            .map(mov -> EstadoCuentaResponse.MovimientoReporteDto.builder()
                                    .fecha(mov.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                                    .tipoMovimiento(mov.getTipoMovimiento())
                                    .valor(mov.getValor())
                                    .saldoDisponible(mov.getSaldo())
                                    .build())
                            .collectList()
                            .map(movimientos -> EstadoCuentaResponse.CuentaReporteDto.builder()
                                    .numeroCuenta(cuenta.getNumeroCuenta())
                                    .tipoCuenta(cuenta.getTipo())
                                    .saldoInicial(cuenta.getSaldoInicial())
                                    .estado(cuenta.getEstado())
                                    .saldoActual(cuenta.getSaldo())
                                    .movimientos(movimientos)
                                    .build());
                })
                .collectList();

        return Mono.zip(clienteMono, cuentasReporteMono)
                .map(tuple -> EstadoCuentaResponse.builder()
                        .clienteId(tuple.getT1().getClienteId())
                        .clienteNombre(tuple.getT1().getNombre())
                        .cuentas(tuple.getT2())
                        .build());
    }
}