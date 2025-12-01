package com.banco.pruebatecnica.mapper;

import com.banco.pruebatecnica.dto.CuentaRequest;
import com.banco.pruebatecnica.dto.CuentaResponse;
import com.banco.pruebatecnica.entity.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CuentaMapper {

    Cuenta toEntity(CuentaRequest dto);

    CuentaResponse toResponse(Cuenta entity);

    void updateEntityFromRequest(CuentaRequest dto, @MappingTarget Cuenta entity);
}