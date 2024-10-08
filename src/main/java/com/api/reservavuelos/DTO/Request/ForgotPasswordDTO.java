package com.api.reservavuelos.DTO.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ForgotPasswordDTO {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com|hotmail\\.com|icloud\\.com)$", message = "El correo electronico no es valido")
    @NotEmpty(message = "El correo Electronico es requerido!")
    private String email;
}
