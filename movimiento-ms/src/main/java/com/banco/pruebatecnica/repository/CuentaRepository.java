package com.banco.pruebatecnica.repository;

import com.banco.pruebatecnica.entity.Cuenta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface CuentaRepository extends ReactiveCrudRepository<Cuenta, Long> {

    // Buscar por número de cuenta (clave única)
    Mono<Cuenta> findByNumeroCuenta(String numeroCuenta);

    // F4: Reporte: Buscar cuentas por Cliente ID
    Flux<Cuenta> findAllByClienteId(Long clienteId);
}