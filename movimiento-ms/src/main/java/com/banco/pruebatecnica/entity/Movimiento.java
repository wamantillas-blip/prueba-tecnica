package com.banco.pruebatecnica.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("movimientos")
public class Movimiento {

    @Id
    private Long id;

    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private String numeroCuenta;
}