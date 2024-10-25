package com.api.reservavuelos.DTO.Cache;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VueloCacheDTO implements Serializable {
    private Long idVuelo;
    private String aerolinea;
    private int numeroVuelo;
    private String tipoAvion;
    private String origen;
    private String destino;
    private LocalDate fechaIda;
    private LocalTime horaSalida;
    private LocalDate fechaVuelta;
    private LocalTime horaVuelta;
    private String duracion;
    private String estadoVuelo;
    private int bussinessClass;
    private int economyClass;
    private BigDecimal precioBussiness;
    private BigDecimal precioEconomy;


}
