package com.api.reservavuelos.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String timeStamp;
    private String code;
    private Long idUsuario;
    private String message;
    private String url;
}
