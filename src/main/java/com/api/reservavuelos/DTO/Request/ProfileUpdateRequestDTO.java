package com.api.reservavuelos.DTO.Request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

@Data
public class ProfileUpdateRequestDTO {
Optional<String> primer_nombre = Optional.empty();
Optional<String> segundo_nombre = Optional.empty();
Optional<String> primer_apellido = Optional.empty();
Optional<String> segundo_apellido = Optional.empty();
Optional<String> email = Optional.empty();
Optional<String> telefono = Optional.empty();
Optional<LocalDate> fecha_nacimiento = Optional.empty();
Optional<String> genero = Optional.empty();

}
