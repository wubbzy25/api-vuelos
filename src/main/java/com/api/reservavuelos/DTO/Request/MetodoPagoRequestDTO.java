package com.api.reservavuelos.DTO.Request;

import com.api.reservavuelos.annotations.CardNumberValidatorAnnotaion;
import com.api.reservavuelos.annotations.ValidExpirationDate;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MetodoPagoRequestDTO {
    @NotEmpty(message = "El nombre del titular es requerido")
    private String nombreTitular;
    @CardNumberValidatorAnnotaion
    @NotEmpty(message = "El numero de la tarjeta es requerido")
    private String numero;
    @NotEmpty(message = "La fecha de expiración es requerida")
    @Pattern(regexp = "^\\d{2}/(0[1-9]|1[0-2])$", message = "La fecha de expiración debe tener el formato yy/MM")
    @ValidExpirationDate
    private String fechaExpiracion;
}
