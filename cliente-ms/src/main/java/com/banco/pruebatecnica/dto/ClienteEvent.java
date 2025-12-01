package com.banco.pruebatecnica.dto;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

@Data
@Builder
public class ClienteEvent implements Serializable {
    private Long clienteId;
    private String identificacion;
    private String nombre;
    private Boolean estado;
    private String tipoEvento;
}