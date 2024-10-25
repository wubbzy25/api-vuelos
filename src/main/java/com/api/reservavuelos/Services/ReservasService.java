package com.api.reservavuelos.Services;

import com.api.reservavuelos.DTO.Request.ReservaRequestDTO;
import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.DTO.Response.VuelosResponseDTO;
import com.api.reservavuelos.Models.Reservas;
import com.api.reservavuelos.Models.Vuelos;
import com.api.reservavuelos.Repositories.ReservasRepository;
import com.api.reservavuelos.Repositories.VuelosRepository;
import com.api.reservavuelos.Utils.DateFormatter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ReservasService {

    private final DateFormatter dateFormatter;
    private final VuelosRepository vuelosRepository;
    private final ReservasRepository reservasRepository;

    public ReservasService(DateFormatter dateFormatter,
                           VuelosRepository vuelosRepository, ReservasRepository reservasRepository) {
        this.dateFormatter = dateFormatter;
        this.vuelosRepository = vuelosRepository;
        this.reservasRepository = reservasRepository;
    }

    public ResponseDTO reservarVuelo(Long id_vuelo, ReservaRequestDTO reservaRequestDTO, HttpServletRequest request){
        Optional<Vuelos> vueloOptional = vuelosRepository.findById(id_vuelo);
        if (vueloOptional.isEmpty()){
            throw new IllegalArgumentException("El vuelo no existe");
        }
        Vuelos vuelo = vueloOptional.get();
        Optional<Reservas> asiento_reserva = reservasRepository.findByNumeroAsiento(reservaRequestDTO.getNumero_asiento());
        if (asiento_reserva.isPresent()){
            throw new IllegalArgumentException("El asiento está reservado");
        }
        int getBussinessCapacity = vuelo.getBussinessClass();
        int getEconomyCapacity = vuelo.getEconomyClass();
        int maxCapacity = getBussinessCapacity + getEconomyCapacity;
        int reservasBussinesClass = reservasRepository.getBussinessClassByidVuelo(id_vuelo);
        int reservasEconomyClass = reservasRepository.getEconomyClassByidVuelo(id_vuelo);
        if (reservasBussinesClass + reservasEconomyClass >= maxCapacity) {
            throw new IllegalArgumentException("El vuelo está completamente ocupado");
        }
        if (reservasBussinesClass >= getBussinessCapacity){
            throw new IllegalArgumentException("Los asientos de bussines class ya estan completamente llenos");
        }
        if (reservasEconomyClass >= getEconomyCapacity){
            throw new IllegalArgumentException("Los asientos economy class ya estan completamente llenos");
        }
        if(Objects.equals(reservaRequestDTO.getClase(), "economy") && reservaRequestDTO.getNumero_asiento() > getEconomyCapacity){
            throw new IllegalArgumentException("Este asiento no existe o no pertenece a la clase economy");
        }
        if (Objects.equals(reservaRequestDTO.getClase(), "bussiness") && reservaRequestDTO.getNumero_asiento() > getBussinessCapacity){
            throw new IllegalArgumentException("Este asiento no existe o no pertenece a la clase bussiness");
        }
        Reservas nuevaReserva = new Reservas();

        return setResponseDTO("P-201", "Se ha reservado el vuelo correctamente", request);
    }
    private ResponseDTO setResponseDTO(String code, String message, HttpServletRequest request){
        return new ResponseDTO(dateFormatter.formatearFecha(), code, message, request.getRequestURI());
    }

}
