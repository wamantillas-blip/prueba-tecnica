package com.banco.pruebatecnica.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class EstadoCuentaResponse {

    private String clienteNombre;
    private Long clienteId;

    private List<CuentaReporteDto> cuentas;

    @Data
    @Builder
    public static class CuentaReporteDto {
        private String numeroCuenta;
        private String tipoCuenta;
        private BigDecimal saldoInicial;
        private Boolean estado;
        private BigDecimal saldoActual;

        private List<MovimientoReporteDto> movimientos;
    }

    @Data
    @Builder
    public static class MovimientoReporteDto {
        private String fecha;
        private String tipoMovimiento;
        private BigDecimal valor;
        private BigDecimal saldoDisponible;
    }
}