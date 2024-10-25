package com.api.reservavuelos.Services;

import com.api.reservavuelos.DTO.Cache.VueloCacheDTO;
import com.api.reservavuelos.DTO.Request.VueloUpdateStateRequestDTO;
import com.api.reservavuelos.DTO.Request.VuelosRequestDTO;
import com.api.reservavuelos.DTO.Request.VuelosUpdateRequestDTO;
import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.DTO.Response.VuelosResponseDTO;
import com.api.reservavuelos.Mappers.VueloMapper;
import com.api.reservavuelos.Models.Vuelos;
import com.api.reservavuelos.Repositories.VuelosRepository;
import com.api.reservavuelos.Utils.DateFormatter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class VuelosService {

    private final VuelosRepository vuelosRepository;
    private final DateFormatter dateFormatter;
    private final RedisTemplate<String, Object>  redisVuelosTemplate;
    private final VueloMapper vueloMapper;
    @Autowired
    public VuelosService(VuelosRepository vuelosRepository,
                         DateFormatter dateFormatter,
                         RedisTemplate<String, Object>  redisVuelosTemplate,
                         VueloMapper vueloMapper) {
           this.vuelosRepository = vuelosRepository;
           this.dateFormatter = dateFormatter;
           this. redisVuelosTemplate =  redisVuelosTemplate;
           this.vueloMapper = vueloMapper;

    }

    public List<VueloCacheDTO> obtenerVuelos(){
        try {
            List<VueloCacheDTO> vuelosCache = (List<VueloCacheDTO>)  redisVuelosTemplate.opsForValue().get("VuelosCache");
            if (vuelosCache != null ){
                return vuelosCache;
            }
           List<Vuelos> vuelos = vuelosRepository.findAll();
            redisVuelosTemplate.opsForValue().set("VuelosCache", vuelos, 1, TimeUnit.HOURS);
           return vueloMapper.vuelostoVuelosCacheDTO(vuelos);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public VuelosResponseDTO obtenerVuelo(Long id_vuelo){
        Optional<Vuelos> vueloOptional = vuelosRepository.findById(id_vuelo);
        if (vueloOptional.isEmpty()){
            throw new IllegalArgumentException("El vuelo no existe");
        }

        Vuelos vuelo = vueloOptional.get();
        return SetVueloResponseDTO(vuelo);
    }

    private VuelosResponseDTO SetVueloResponseDTO(Vuelos vuelo) {
        VuelosResponseDTO vueloResponse = new VuelosResponseDTO();
        vueloResponse.setIdVuelo(vuelo.getIdVuelo());
        vueloResponse.setAerolinea(vuelo.getAerolinea());
        vueloResponse.setNumeroVuelo(vuelo.getNumeroVuelo());
        vueloResponse.setTipoAvion(vuelo.getTipoAvion());
        vueloResponse.setOrigen(vuelo.getOrigen());
        vueloResponse.setDestino(vuelo.getDestino());
        vueloResponse.setFechaIda(vuelo.getFechaIda());
        vueloResponse.setHoraSalida(vuelo.getHoraSalida());
        vueloResponse.setFechaVuelta(vuelo.getFechaVuelta());
        vueloResponse.setHoraVuelta(vuelo.getHoraVuelta());
        vueloResponse.setDuracion(vuelo.getDuracion());
        vueloResponse.setEstadoVuelo(vuelo.getEstadoVuelo());
        vueloResponse.setBussinessClass(vuelo.getBussinessClass());
        vueloResponse.setEconomyClass(vuelo.getEconomyClass());
        vueloResponse.setPrecioBussiness(vuelo.getPrecioBussiness());
        vueloResponse.setPrecioEconomy(vuelo.getPrecioEconomy());
        return vueloResponse;
    }

    public ResponseDTO crearVuelo(VuelosRequestDTO vueloRequest, HttpServletRequest request){
        try {
           Optional<Vuelos> vueloOptional = vuelosRepository.getVuelosByNumeroVuelo(vueloRequest.getNumeroVuelo());
           if (vueloOptional.isPresent()){
               throw new IllegalArgumentException("El vuelo ya existe");
           }
           Vuelos vuelo = setVuelo(vueloRequest);
           vuelosRepository.save(vuelo);
           setListVueloCache();
           return setResponseDTO("P-201", "Vuelo creado correctamente", request);
        } catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Vuelos setVuelo(VuelosRequestDTO vueloRequest){
        Vuelos vuelo = new Vuelos();
        vuelo.setAerolinea(vueloRequest.getAerolinea());
        vuelo.setNumeroVuelo(vueloRequest.getNumeroVuelo());
        vuelo.setTipoAvion(vueloRequest.getTipoAvion());
        vuelo.setOrigen(vueloRequest.getOrigen());
        vuelo.setDestino(vueloRequest.getDestino());
        vuelo.setFechaIda(vueloRequest.getFechaIda());
        vuelo.setHoraSalida(vueloRequest.getHoraSalida());
        vuelo.setFechaVuelta(vueloRequest.getFechaVuelta());
        vuelo.setHoraVuelta(vueloRequest.getHoraVuelta());
        vuelo.setDuracion(vueloRequest.getDuracion());
        vuelo.setBussinessClass(vueloRequest.getBussinessClass());
        vuelo.setEconomyClass(vueloRequest.getEconomyClass());
        vuelo.setPrecioBussiness(vueloRequest.getPrecioBussiness());
        vuelo.setPrecioEconomy(vueloRequest.getPrecioEconomy());
        return vuelo;
    }
    public ResponseDTO actualizarEstadoVuelo(Long id_vuelo, VueloUpdateStateRequestDTO estado, HttpServletRequest request){
        Optional<Vuelos> vueloOptional = vuelosRepository.findById(id_vuelo);
        if (vueloOptional.isEmpty()){
            throw new IllegalArgumentException("El vuelo no existe");
        }
        Vuelos vuelo = vueloOptional.get();
        vuelo.setEstadoVuelo(estado.getEstado());
        vuelosRepository.save(vuelo);
        setListVueloCache();
      return setResponseDTO("P-200", "El estado del vuelo fue actualizado correctamente", request);
     }

     public ResponseDTO actualizarInformacionVuelo(Long id_vuelo, VuelosUpdateRequestDTO vueloDTO, HttpServletRequest request){
        Optional<Vuelos> VueloOptional = vuelosRepository.findById(id_vuelo);
        if (VueloOptional.isEmpty()){
            throw new IllegalArgumentException("El vuelo no existe");
        }
        Vuelos vueloResult = VueloOptional.get();
        Vuelos vuelo = setUpdateVueloDTO(vueloResult, vueloDTO);
        vuelosRepository.save(vuelo);
        setListVueloCache();
         return setResponseDTO("P-200", "La informacion del vuelo fue actualizada correctamente", request);
     }
     private Vuelos setUpdateVueloDTO(Vuelos vuelo, VuelosUpdateRequestDTO vueloDTO){
         vueloDTO.getAerolinea().ifPresent(vuelo::setAerolinea);
         vueloDTO.getNumeroVuelo().ifPresent(vuelo::setNumeroVuelo);
         vueloDTO.getTipoAvion().ifPresent(vuelo::setTipoAvion);
         vueloDTO.getOrigen().ifPresent(vuelo::setOrigen);
         vueloDTO.getDestino().ifPresent(vuelo::setDestino);
         vueloDTO.getFechaIda().ifPresent(vuelo::setFechaIda);
         vueloDTO.getHoraSalida().ifPresent(vuelo::setHoraSalida);
         vueloDTO.getFechaVuelta().ifPresent(vuelo::setFechaVuelta);
         vueloDTO.getHoraVuelta().ifPresent(vuelo::setHoraVuelta);
         vueloDTO.getDuracion().ifPresent(vuelo::setDuracion);
         vueloDTO.getBussinessClass().ifPresent(vuelo::setBussinessClass);
         vueloDTO.getEconomyClass().ifPresent(vuelo::setEconomyClass);
         vueloDTO.getPrecioBussiness().ifPresent(vuelo::setPrecioBussiness);
         vueloDTO.getPrecioEconomy().ifPresent(vuelo::setPrecioEconomy);
         return vuelo;
     }

    private ResponseDTO setResponseDTO(String code, String message, HttpServletRequest request){
        return new ResponseDTO(dateFormatter.formatearFecha(), code, message, request.getRequestURI());
    }
  private void setListVueloCache() {
     try {
         List<VueloCacheDTO> VuelosCache = (List<VueloCacheDTO>)  redisVuelosTemplate.opsForValue().get("VuelosCache");
         if (VuelosCache != null) {
             redisVuelosTemplate.delete("VuelosCache");
             List<Vuelos> vuelosActualizados = vuelosRepository.findAll();
             redisVuelosTemplate.opsForValue().set("VuelosCache", vuelosActualizados,1, TimeUnit.HOURS);
         }
     } catch (Exception e) {
         throw  new RuntimeException(e);
     }
 }
    public ResponseDTO eliminarVuelo(Long id_vuelo, HttpServletRequest request){
        try {
            Optional<Vuelos> vueloOptional = vuelosRepository.findById(id_vuelo);
            if (vueloOptional.isEmpty()){
                throw new IllegalArgumentException("Este vuelo no existe");
            }
                vuelosRepository.deleteById(id_vuelo);
                setListVueloCache();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return setResponseDTO("P-200", "El vuelo fue eliminado correctamente", request);
    }
}