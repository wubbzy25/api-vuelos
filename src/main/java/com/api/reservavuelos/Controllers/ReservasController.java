package com.api.reservavuelos.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/reservas/")
public class ReservasController {

    @GetMapping("/vuelos")
    public ResponseEntity<String> getVuelos(){
        return new ResponseEntity<>("Los vuelos disponibles son: ", HttpStatus.OK);
    }
}
