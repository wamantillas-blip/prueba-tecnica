package com.banco.pruebatecnica.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MovimientoResponse {
    private Long id;
    private LocalDateTime fecha;
    private String tipoMovimiento; // DEPOSITO o RETIRO
    private BigDecimal valor; // El monto depositado o retirado
    private BigDecimal saldoDisponible; // Saldo de la cuenta después de este movimiento
    private String numeroCuenta;
}