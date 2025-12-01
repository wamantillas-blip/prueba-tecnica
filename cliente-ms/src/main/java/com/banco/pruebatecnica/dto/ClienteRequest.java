package com.banco.pruebatecnica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteRequest {

    // Campos de Persona
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String genero;

    @NotNull(message = "La edad es obligatoria")
    private Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    private String identificacion;

    private String direccion;
    private String telefono;

    // Campos de Cliente
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    private String contrasena;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}