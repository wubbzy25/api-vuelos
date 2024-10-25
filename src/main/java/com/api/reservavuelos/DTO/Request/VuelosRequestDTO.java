package com.api.reservavuelos.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VuelosRequestDTO {
    @NotNull
    private String aerolinea;
    @NotNull
    private int numeroVuelo;
    @NotNull
    private String tipoAvion;
    @NotNull
    @Pattern(regexp = "[A-Z]{3}")
    private String origen;
    @NotNull
    @Pattern(regexp = "[A-Z]{3}")
    private String destino;
    @NotNull
    @Future
    private LocalDate fechaIda;
    @NotNull
    @Future
    private LocalTime horaSalida;
    @NotNull
    @Future
    private LocalDate fechaVuelta;
    @NotNull
    @Future
    private LocalTime horaVuelta;
    @NotNull
    private String duracion;
    @NotNull
    @Min(1)
    private int bussinessClass;
    @NotNull
    @Min(1)
    private int economyClass;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioBussiness;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal precioEconomy;
}
