package com.api.reservavuelos.Controllers;

import com.api.reservavuelos.DTO.Request.ReservaRequestDTO;
import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.Services.ReservasService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reservas")
public class ReservasController {

    private final ReservasService reservasService;

    public ReservasController(ReservasService reservasService){
        this.reservasService = reservasService;
    }

    @PostMapping("/reservar")
    public ResponseEntity<ResponseDTO> reservarVuelo(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO, HttpServletRequest request){
        System.out.println(reservaRequestDTO );
        long id_vuelo = 1;
        return new ResponseEntity<>(reservasService.reservarVuelo(id_vuelo, reservaRequestDTO, request), HttpStatus.OK);
    }
}
