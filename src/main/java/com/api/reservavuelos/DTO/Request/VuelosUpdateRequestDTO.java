package com.api.reservavuelos.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Data
public class VuelosUpdateRequestDTO {
 private Optional<@NotEmpty(message = "La aerolinea no puede estar vacia")  String> aerolinea =  Optional.empty();
 private Optional<@NotNull Integer> numeroVuelo =  Optional.empty();
 private Optional<@NotNull String> tipoAvion =  Optional.empty();
 private Optional<@NotNull @Pattern(regexp = "[A-Z]{3}") String> origen =  Optional.empty();
 private Optional<@NotNull @Pattern(regexp = "[A-Z]{3}") String> destino =  Optional.empty();
 private Optional<@NotNull @Future LocalDate> fechaIda =  Optional.empty();
 private Optional<@NotNull @Future LocalTime> horaSalida =  Optional.empty();
 private Optional<@NotNull@Future LocalDate> fechaVuelta =  Optional.empty();
 private Optional<@NotNull @Future LocalTime> horaVuelta =  Optional.empty();
 private Optional<@NotNull String> duracion =  Optional.empty();
 private Optional<@NotNull @Min(1) Integer> bussinessClass =  Optional.empty();
 private Optional<@NotNull @Min(1) Integer> economyClass =  Optional.empty();
 private Optional<@NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal> precioBussiness =  Optional.empty();
 private Optional<@NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal> precioEconomy =  Optional.empty();
}
