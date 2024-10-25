package com.api.reservavuelos.Controllers;

import com.api.reservavuelos.DTO.Request.MetodoPagoRequestDTO;
import com.api.reservavuelos.DTO.Response.MetodosPagosResponseDTO;
import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.Services.MetodosPagosService;
import com.api.reservavuelos.Utils.DateFormatter;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestControllerAdvice
@RequestMapping("/api/v1/metodos")
public class MetodoController {

    private final MetodosPagosService metodosPagosService;
    private final DateFormatter dateFormatter;

    @Autowired
    public MetodoController(MetodosPagosService metodosPagosService, DateFormatter dateFormatter) {
       this.metodosPagosService = metodosPagosService;
       this.dateFormatter = dateFormatter;
    }

    @GetMapping("/{id_usuario}")
    public String getMetodos(@PathVariable Long id_usuario, HttpServletRequest request) throws Exception {
        return metodosPagosService.getMetodosPagos(id_usuario, request);
    }

    @GetMapping("/{id_usuario}/{id_metodo}")
    public MetodosPagosResponseDTO getMetodoPorId(@PathVariable Long id_usuario, @PathVariable Long id_metodo, HttpServletRequest request){
        return metodosPagosService.getMetodoPago(id_usuario, id_metodo,request);
    }

    @PostMapping("/crear/{id_usuario}")
    public ResponseEntity<ResponseDTO> crearMetodo(@PathVariable Long id_usuario, @Valid @RequestBody MetodoPagoRequestDTO metodoPagoRequestDTO, HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(metodosPagosService.crearMetodoPago(id_usuario, metodoPagoRequestDTO,request), HttpStatus.CREATED);
    }
    @PutMapping("/editar/{id_usuario}/{id_metodo}")
    public ResponseEntity<ResponseDTO> editarMetodo(@PathVariable Long id_usuario, @PathVariable Long id_metodo, @Valid @RequestBody MetodoPagoRequestDTO metodoPagoRequestDTO, HttpServletRequest request){
        return new ResponseEntity<>(metodosPagosService.editarmetodopago(id_usuario,id_metodo,request, metodoPagoRequestDTO),HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id_usuario}/{id_metodo}")
    public ResponseEntity<ResponseDTO> eliminarMetodo(@PathVariable Long id_usuario, @PathVariable Long id_metodo, HttpServletRequest request){
        return new ResponseEntity<>(metodosPagosService.eliminarMetodoPago(id_usuario,id_metodo,request),HttpStatus.OK);
    }

}
