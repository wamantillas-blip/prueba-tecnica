package com.banco.pruebatecnica.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("cuentas")
public class Cuenta {

    @Id
    private Long id;

    private String numeroCuenta;
    private String tipo;
    private BigDecimal saldoInicial;
    private BigDecimal saldo;
    private Boolean estado;
    private Long clienteId;
}