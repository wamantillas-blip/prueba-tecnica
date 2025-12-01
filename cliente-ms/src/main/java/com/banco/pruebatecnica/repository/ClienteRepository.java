package com.banco.pruebatecnica.repository;

import com.banco.pruebatecnica.entity.Cliente;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@Repository
public interface ClienteRepository extends ReactiveCrudRepository<Cliente, Long> {

    Mono<Cliente> findByIdentificacion(String identificacion);
    Flux<Cliente> findByEstado(Boolean estado);
}
