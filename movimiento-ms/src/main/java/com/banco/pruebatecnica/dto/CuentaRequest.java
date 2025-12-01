package com.banco.pruebatecnica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CuentaRequest {

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio (Ahorros/Corriente)")
    private String tipo;

    @NotNull(message = "El saldo inicial es obligatorio")
    @PositiveOrZero(message = "El saldo inicial no puede ser negativo")
    private BigDecimal saldoInicial;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;
}