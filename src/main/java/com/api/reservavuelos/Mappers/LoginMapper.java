package com.api.reservavuelos.Mappers;


import com.api.reservavuelos.DTO.LoginDTO;
import com.api.reservavuelos.Models.Usuarios;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoginMapper {
    LoginMapper INSTANCE = Mappers.getMapper(LoginMapper.class);

    LoginDTO ModelToDTO(Usuarios usuarios);
    Usuarios DTOToModel(LoginMapper loginMapper);

}
