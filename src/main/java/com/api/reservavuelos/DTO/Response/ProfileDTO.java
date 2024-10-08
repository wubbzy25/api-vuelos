package com.api.reservavuelos.DTO.Response;

import lombok.Data;

import java.util.Date;

@Data
public class ProfileDTO {
    private String url_imagen;
    private String nombre;
    private String email;
    private String telefono;
    private Date fecha_nacimiento;
    private String genero;
}
