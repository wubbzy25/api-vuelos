package com.api.reservavuelos.DTO.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CodigoRequestDTO {
     @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com|hotmail\\.com|icloud\\.com)$", message = "El correo electronico no es valido")
     @NotEmpty(message = "El correo Electronico es requerido!")
     private String email;
     @NotEmpty(message = "El codigo no puede estar vacio")
     @Size(min = 6, max = 6)
     private String codigo;
}
