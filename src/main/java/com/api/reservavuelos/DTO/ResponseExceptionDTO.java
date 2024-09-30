package com.api.reservavuelos.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseExceptionDTO {
    private String timeStamp;
    private String code;
    private String message;
    private String url;
}
