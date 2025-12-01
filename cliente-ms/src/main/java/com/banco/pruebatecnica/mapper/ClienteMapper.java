package com.banco.pruebatecnica.mapper;

import com.banco.pruebatecnica.dto.ClienteRequest;
import com.banco.pruebatecnica.dto.ClienteResponse;
import com.banco.pruebatecnica.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper {

    Cliente toEntity(ClienteRequest dto);

    @Mapping(source = "id", target = "clienteId")
    ClienteResponse toResponse(Cliente entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contrasena", ignore = true)
    void updateEntityFromRequest(ClienteRequest dto, @MappingTarget Cliente entity);
}