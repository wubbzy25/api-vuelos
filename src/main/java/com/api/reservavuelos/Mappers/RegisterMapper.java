package com.api.reservavuelos.Mappers;


import com.api.reservavuelos.DTO.RegisterDTO;
import com.api.reservavuelos.Models.Usuarios;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    RegisterDTO ModelToDTO(Usuarios usuario);
    Usuarios DTOToModel(RegisterDTO registerDTO);
}
