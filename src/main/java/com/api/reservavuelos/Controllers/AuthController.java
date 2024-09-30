package com.api.reservavuelos.Controllers;
import com.api.reservavuelos.DTO.ForgotPasswordDTO;
import com.api.reservavuelos.DTO.LoginDTO;
import com.api.reservavuelos.DTO.RegisterDTO;
import com.api.reservavuelos.Services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

  @Autowired
  private AuthService authService;
    @PostMapping("login")
    public ResponseEntity<String> IniciarSesion(@Valid @RequestBody LoginDTO loginDto){
      String token = authService.login(loginDto);
      return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<String> Registrar(@Valid @RequestBody RegisterDTO registerDTO){
        authService.registrarUsuario(registerDTO);
        return new ResponseEntity<>("Usuario registrado con exito", HttpStatus.CREATED);
    }


    @GetMapping("forgot-password")
    public ResponseEntity<String> OlvidarContraseña(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDto){
     authService.OlvidarContraseña(forgotPasswordDto);
    return new ResponseEntity<>("Codigo Enviado Al correo electronico registrado", HttpStatus.OK);
    }


   @GetMapping("logout")
    public ResponseEntity<String> CerrarSesion(){
     return new ResponseEntity<>("Usuario deslogeado", HttpStatus.OK);
    }

}

