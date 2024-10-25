package com.api.reservavuelos.DTO.Request;

import com.api.reservavuelos.Utils.VueloEstado;
import com.api.reservavuelos.annotations.ValidEstadoVuelo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VueloUpdateStateRequestDTO {
    @NotNull
    @ValidEstadoVuelo
    private VueloEstado Estado;
}
