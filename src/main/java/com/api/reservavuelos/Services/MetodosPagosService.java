package com.api.reservavuelos.Services;


import com.api.reservavuelos.DTO.Request.MetodoPagoRequestDTO;
import com.api.reservavuelos.DTO.Response.MetodosPagosResponseDTO;
import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.Exceptions.UserNotFoundException;
import com.api.reservavuelos.Models.Metodos_pagos;
import com.api.reservavuelos.Models.Usuarios;
import com.api.reservavuelos.Repositories.MetodosPagosRepository;
import com.api.reservavuelos.Repositories.UsuarioRepository;
import com.api.reservavuelos.Utils.DateFormatter;
import com.api.reservavuelos.Utils.Encryptation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.micrometer.core.instrument.config.validate.Validated;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MetodosPagosService {

    private final UsuarioRepository usuarioRepository;
    private final SecretKey secretKey;
    private final DateFormatter dateFormatter;
    private final MetodosPagosRepository metodosPagosRepository;
    private final Encryptation encryptation;

    @Autowired
    public MetodosPagosService(UsuarioRepository usuarioRepository,
                              MetodosPagosRepository metodosPagosRepository,
                               DateFormatter dateFormatter,
                               Encryptation encryptation,
                               SecretKey secretKey) {
        this.usuarioRepository = usuarioRepository;
        this.metodosPagosRepository = metodosPagosRepository;
        this.dateFormatter = dateFormatter;
        this.encryptation = encryptation;
        this.secretKey = secretKey;
    }
    public ResponseDTO crearMetodoPago(Long usuario_id, MetodoPagoRequestDTO metodoPagoRequestDTO, HttpServletRequest request) throws Exception {
       List<Metodos_pagos> metodos = metodosPagosRepository.obtenermetodosdepago(usuario_id);
       if (verificarNumero(metodos, metodoPagoRequestDTO.getNumero())){
           throw new IllegalArgumentException("Parece que ya tienes registrada este metodo de pago");
       }
        Optional<Usuarios> usuarioOptional = usuarioRepository.findById(usuario_id);
        Metodos_pagos metodoPago = getMetodosPagosForCreating(metodoPagoRequestDTO, usuarioOptional, encryptation,secretKey);
        metodosPagosRepository.save(metodoPago);
        return new ResponseDTO(dateFormatter.formatearFecha(), "P-201", "Metodo de pago creado correctamente", request.getRequestURI());
    }

    private static Metodos_pagos getMetodosPagosForCreating(MetodoPagoRequestDTO metodoPagoRequestDTO, Optional<Usuarios> usuarioOptional, Encryptation encryptation, SecretKey secretKey) {
        if (usuarioOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        try {
            Usuarios usuario = usuarioOptional.get();
            Metodos_pagos metodoPago = new Metodos_pagos();

            metodoPago.setNombreTitular(metodoPagoRequestDTO.getNombreTitular());
            metodoPago.setNumero(encryptation.encrypt(metodoPagoRequestDTO.getNumero(), secretKey));
            metodoPago.setFechaVencimiento(encryptation.encrypt(metodoPagoRequestDTO.getFechaExpiracion(), secretKey));
            metodoPago.setUsuarios(usuario);
            return metodoPago;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el metodo de pago", e);
        }
    }

    public String getMetodosPagos(Long id_usuario, HttpServletRequest request) throws Exception {
        List<Metodos_pagos> result = metodosPagosRepository.obtenermetodosdepago(id_usuario);
        List<MetodosPagosResponseDTO> metodoPagos = new ArrayList<>();
        if (result.isEmpty())  {
           throw new RuntimeException("El usuario no tiene metodos de pagos");
        }
        for (Metodos_pagos metodoResult : result) {
           MetodosPagosResponseDTO metodoPagoResponseDTO = new MetodosPagosResponseDTO();
           metodoPagoResponseDTO.setIdMetodoPago(metodoResult.getIdMetodoPago());
           metodoPagoResponseDTO.setNombreTitular(metodoResult.getNombreTitular());
           metodoPagoResponseDTO.setNumero(encryptation.decrypt(metodoResult.getNumero(), secretKey));
           metodoPagoResponseDTO.setFechaExpiracion(encryptation.decrypt(metodoResult.getFechaVencimiento(), secretKey));
           metodoPagos.add(metodoPagoResponseDTO);
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        return writer.writeValueAsString(metodoPagos);
    }
    public MetodosPagosResponseDTO getMetodoPago(Long id_usuario, Long id_metodoPago, HttpServletRequest request){
        try {
            Metodos_pagos metodo = metodosPagosRepository.obtenermetododepago(id_usuario,id_metodoPago);
            MetodosPagosResponseDTO metodoPagoDto = new MetodosPagosResponseDTO();
            metodoPagoDto.setIdMetodoPago(metodo.getIdMetodoPago());
            metodoPagoDto.setNombreTitular(metodo.getNombreTitular());
            metodoPagoDto.setNumero(encryptation.decrypt(metodo.getNumero(), secretKey));
            metodoPagoDto.setFechaExpiracion(encryptation.decrypt(metodo.getFechaVencimiento(), secretKey));
            return metodoPagoDto;
        } catch (Exception e){
            throw new IllegalArgumentException("El usuario no tiene este metodo de pago");
        }
    }

    public ResponseDTO editarmetodopago(Long id_usuario, Long id_metodoPago, HttpServletRequest request, MetodoPagoRequestDTO metodoPagoRequestDTO){
        try {
            Metodos_pagos metodo = metodosPagosRepository.obtenermetododepago(id_usuario,id_metodoPago);
            metodo.setNombreTitular(metodoPagoRequestDTO.getNombreTitular());
            metodo.setNumero(metodoPagoRequestDTO.getNumero());
            metodo.setFechaVencimiento(metodoPagoRequestDTO.getFechaExpiracion());
            metodosPagosRepository.save(metodo);
            return setResponseDTO("P-200", "Metodo de pago editado correctamente", request);
        }catch (Exception e){
            throw new IllegalArgumentException("Error al editar el metodo de pago");
        }
    }
    public ResponseDTO eliminarMetodoPago(Long id_usuario, Long id_metodoPago, HttpServletRequest request) {
        Metodos_pagos metodo = metodosPagosRepository.obtenermetododepago(id_usuario, id_metodoPago);
        if (metodo == null || !metodo.getUsuarios().getId().equals(id_usuario)) {
            throw new IllegalArgumentException("El método de pago no pertenece al usuario especificado");
        }
        metodosPagosRepository.deleteById(id_metodoPago);
        return setResponseDTO("P-200", "Metodo de de pago eliminado correctamente", request);
    }

    private ResponseDTO setResponseDTO(String code, String message, HttpServletRequest request){
        return new ResponseDTO(dateFormatter.formatearFecha(), code, message, request.getRequestURI());
    }
    private Boolean verificarNumero(List<Metodos_pagos> metodos, String numero){
        for( Metodos_pagos metodo: metodos){
            if(metodo.getNumero().equals(numero)){
                return true;
            }
        }
        return false;
    }

}
