package com.banco.pruebatecnica.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("CLIENTES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    private Long id;

    private String nombre;
    private String genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private String contrasena;
    private Boolean estado;
}
