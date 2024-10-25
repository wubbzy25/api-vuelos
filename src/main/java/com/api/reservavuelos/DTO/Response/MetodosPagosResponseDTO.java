package com.api.reservavuelos.DTO.Response;

import lombok.Data;

@Data
public class MetodosPagosResponseDTO {
    private Long IdMetodoPago;
    private String nombreTitular;
    private String numero;
    private String fechaExpiracion;
}
