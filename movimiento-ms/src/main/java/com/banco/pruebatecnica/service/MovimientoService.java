package com.banco.pruebatecnica.service;

import com.banco.pruebatecnica.dto.MovimientoRequest;
import com.banco.pruebatecnica.dto.MovimientoResponse;
import com.banco.pruebatecnica.entity.Movimiento;
import com.banco.pruebatecnica.exception.ResourceNotFoundException;
import com.banco.pruebatecnica.exception.SaldoNoDisponibleException;
import com.banco.pruebatecnica.repository.CuentaRepository;
import com.banco.pruebatecnica.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    private static final String TIPO_DEPOSITO = "DEPOSITO";
    private static final String TIPO_RETIRO = "RETIRO";


    private Mono<BigDecimal> obtenerSaldoActual(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada: " + numeroCuenta)))
                .flatMap(cuenta ->
                        movimientoRepository.findTop1ByNumeroCuentaOrderByFechaDesc(numeroCuenta)
                                .next()
                                .map(Movimiento::getSaldo)
                                .defaultIfEmpty(cuenta.getSaldoInicial())
                );
    }


    public Mono<MovimientoResponse> registrarMovimiento(MovimientoRequest request) {

        return cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada o inactiva.")))
                .flatMap(cuenta ->
                        obtenerSaldoActual(cuenta.getNumeroCuenta())
                                .flatMap(saldoActual -> {
                                    BigDecimal nuevoSaldo = saldoActual.add(request.getValor());

                                    if (request.getValor().compareTo(BigDecimal.ZERO) < 0) {
                                        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
                                            return Mono.error(new SaldoNoDisponibleException("Saldo no disponible"));
                                        }
                                    }
                                    Movimiento nuevoMovimiento = Movimiento.builder()
                                            .fecha(LocalDateTime.now())
                                            .tipoMovimiento(request.getValor().compareTo(BigDecimal.ZERO) > 0 ? TIPO_DEPOSITO : TIPO_RETIRO)
                                            .valor(request.getValor())
                                            .saldo(nuevoSaldo)
                                            .numeroCuenta(cuenta.getNumeroCuenta())
                                            .build();

                                    return movimientoRepository.save(nuevoMovimiento)
                                            .flatMap(savedMovimiento -> {
                                                cuenta.setSaldo(nuevoSaldo);
                                                return cuentaRepository.save(cuenta)
                                                        .thenReturn(savedMovimiento);
                                            })
                                            .map(savedMovimiento -> MovimientoResponse.builder()
                                                    .id(savedMovimiento.getId())
                                                    .numeroCuenta(savedMovimiento.getNumeroCuenta())
                                                    .fecha(savedMovimiento.getFecha())
                                                    .tipoMovimiento(savedMovimiento.getTipoMovimiento())
                                                    .valor(savedMovimiento.getValor())
                                                    .saldoDisponible(savedMovimiento.getSaldo())
                                                    .build());
                                })
                );
    }

    public Flux<MovimientoResponse> obtenerMovimientosPorCuenta(String numeroCuenta) {
        return movimientoRepository.findByNumeroCuenta(numeroCuenta)
                .map(movimiento -> MovimientoResponse.builder()
                        .id(movimiento.getId())
                        .numeroCuenta(movimiento.getNumeroCuenta())
                        .fecha(movimiento.getFecha())
                        .tipoMovimiento(movimiento.getTipoMovimiento())
                        .valor(movimiento.getValor())
                        .saldoDisponible(movimiento.getSaldo())
                        .build());
    }
}