package com.api.reservavuelos.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private String timeStamp;
    private String code;
    private String message;
    private String url;
}
