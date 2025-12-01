package com.banco.pruebatecnica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MovimientoRequest {

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    @NotNull(message = "El valor del movimiento (positivo o negativo) es obligatorio")
    private BigDecimal valor;

}