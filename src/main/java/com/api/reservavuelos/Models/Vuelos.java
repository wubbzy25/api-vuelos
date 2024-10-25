package com.api.reservavuelos.Models;

import com.api.reservavuelos.Utils.VueloEstado;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "vuelos")
public class Vuelos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVuelo;
    @Column
    @NotNull
    private String aerolinea;
    @Column()
    @NotNull
    private int numeroVuelo;
    @Column
    @NotNull
    private String tipoAvion;
    @Column
    @NotNull
    private String origen;
    @Column
    @NotNull
    private String destino;
    @Column
    @NotNull
    private LocalDate fechaIda;
    @Column
    @NotNull
    private LocalTime horaSalida;
    @Column
    private LocalDate fechaVuelta;
    @Column
    private LocalTime horaVuelta;
    @Column
    @NotNull
    private String duracion;
    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private VueloEstado estadoVuelo = VueloEstado.PROGRAMADO;
    @Column
    @NotNull
    private int bussinessClass;
    @Column
    @NotNull
    private int economyClass;
    @Column
    @NotNull
    private BigDecimal precioBussiness;
    @Column
    @NotNull
    private BigDecimal precioEconomy;
    @OneToMany(mappedBy = "vuelos")
    @ToString.Exclude
    private List<Reservas> reservas;
}
