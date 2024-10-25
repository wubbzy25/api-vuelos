package com.api.reservavuelos.DTO.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ProfileRequestDTO {
    @NotEmpty(message = "El campo primer_nombre no puede estar vacio")
    private String primer_nombre;
    @NotEmpty(message = "El campo segundo_nombre no puede estar vacio")
    private String segundo_nombre;
    @NotEmpty(message = "El campo primer_apellidos no puede estar vacio")
    private String primer_apellido;
    @NotEmpty(message = "El campo segundo_apellidos no puede estar vacio")
    private String segundo_apellido;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com|hotmail\\.com|icloud\\.com)$", message = "El correo electronico no es valido")
    @NotEmpty(message = "El correo Electronico es requerido!")
    private String email;
    @NotEmpty(message =  "El campo telefono no puede estar vacio")
    @Pattern(regexp = "\\d{10}", message = "El número de teléfono debe tener exactamente 10 dígitos")
    private String telefono;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha_nacimiento;
    @NotEmpty(message = "El campo genero no puede estar vacio")
    private String genero;
}
