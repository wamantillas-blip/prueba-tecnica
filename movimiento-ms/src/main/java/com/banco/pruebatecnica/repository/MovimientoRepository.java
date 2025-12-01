package com.banco.pruebatecnica.repository;

import com.banco.pruebatecnica.entity.Movimiento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface MovimientoRepository extends ReactiveCrudRepository<Movimiento, Long> {

    Flux<Movimiento> findByNumeroCuentaAndFechaBetween(String numeroCuenta, LocalDateTime inicio, LocalDateTime fin);

    Flux<Movimiento> findTop1ByNumeroCuentaOrderByFechaDesc(String numeroCuenta);

    Flux<Movimiento> findByNumeroCuenta(String numeroCuenta);
}