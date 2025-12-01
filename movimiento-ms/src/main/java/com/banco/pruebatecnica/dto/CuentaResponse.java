package com.banco.pruebatecnica.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CuentaResponse {
    private Long id;
    private String numeroCuenta;
    private String tipo;
    private BigDecimal saldoInicial;
    private BigDecimal saldo;
    private Boolean estado;
    private Long clienteId;
}