package com.banco.pruebatecnica.service;

import com.banco.pruebatecnica.dto.CuentaRequest;
import com.banco.pruebatecnica.dto.CuentaResponse;
import com.banco.pruebatecnica.entity.Cuenta;
import com.banco.pruebatecnica.exception.ResourceNotFoundException;
import com.banco.pruebatecnica.mapper.CuentaMapper;
import com.banco.pruebatecnica.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    public Mono<CuentaResponse> crearCuenta(CuentaRequest request) {
        return cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())
                .flatMap(existing ->
                        Mono.error(new IllegalArgumentException("Ya existe una cuenta con el número: " + request.getNumeroCuenta()))
                )
                .switchIfEmpty(Mono.defer(() -> {
                    Cuenta cuenta = cuentaMapper.toEntity(request);
                    cuenta.setSaldo(request.getSaldoInicial());

                    return cuentaRepository.save(cuenta)
                            .map(cuentaMapper::toResponse);
                }))
                .cast(CuentaResponse.class);
    }

    public Mono<CuentaResponse> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada con ID: " + id)))
                .map(cuentaMapper::toResponse);
    }

    public Mono<CuentaResponse> actualizarCuenta(Long id, CuentaRequest request) {
        return cuentaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cuenta no encontrada con ID: " + id)))
                .flatMap(cuenta -> {
                    cuentaMapper.updateEntityFromRequest(request, cuenta);
                    return cuentaRepository.save(cuenta);
                })
                .map(cuentaMapper::toResponse);
    }

    public Flux<CuentaResponse> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll()
                .map(cuentaMapper::toResponse);
    }
}