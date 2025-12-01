package com.banco.pruebatecnica.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteResponse {

    private Long clienteId; // PK/ClienteID

    // Campos de Persona
    private String nombre;
    private String identificacion;
    private String direccion;
    private String telefono;

    // Campos de Cliente
    private Boolean estado;
}