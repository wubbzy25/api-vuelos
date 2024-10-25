package com.api.reservavuelos.DTO.Cache;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProfileCacheDTO implements Serializable {
    private Long id_usuario;
    private String url_imagen;
    private String primer_nombre;
    private String segundo_nombre;
    private String primer_apellido;
    private String segundo_apellido;
    private String nombre_completo;
    private String email;
    private String telefono;
    private Date fecha_nacimiento;
    private String genero;
}