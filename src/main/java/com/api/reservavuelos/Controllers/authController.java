package com.api.reservavuelos.Controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class authController {

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> IniciarSesion(){

    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> Registrarse(){
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> CerrarSesion(){

    }
}

