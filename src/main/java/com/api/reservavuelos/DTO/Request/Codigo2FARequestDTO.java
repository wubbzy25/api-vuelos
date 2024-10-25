package com.api.reservavuelos.DTO.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class Codigo2FARequestDTO {
    @Min(value = 100000)
    @Max(value = 999999)
    private int codigo;
}
