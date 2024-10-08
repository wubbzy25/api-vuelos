package com.api.reservavuelos.Controllers;
import com.api.reservavuelos.DTO.Request.*;
import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.Services.AuthService;
import com.api.reservavuelos.Utils.DateFormatter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
  private final Date tiempoactual = new Date();
  private final DateFormatter dateFormatter;
  private final AuthService authService;
  @Autowired
  public AuthController(DateFormatter dateFormatter, AuthService authService){
    this.dateFormatter = dateFormatter;
    this.authService = authService;
  }
    @PostMapping("login")
    public ResponseEntity<ResponseDTO> IniciarSesion(@Valid @RequestBody LoginDTO loginDto, HttpServletRequest request){
      String token = authService.login(loginDto);
      return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual), "P-200", token, request.getRequestURI()), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<ResponseDTO> Registrar(@Valid @RequestBody RegisterDTO registerDTO, HttpServletRequest request){
        authService.registrarUsuario(registerDTO);
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual), "P-201", "El usuario ha sido registrado exitosamente", request.getRequestURI() ),HttpStatus.CREATED);
    }


    @GetMapping("forgot-password")
    public ResponseEntity<ResponseDTO> OlvidarContraseña(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDto, HttpServletRequest request){
     authService.OlvidarContraseña(forgotPasswordDto);
    return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual), "P-200", "Codigo enviado al correo electronico registrado", request.getRequestURI()), HttpStatus.OK);
    }

    @PostMapping("verify-code")
  public ResponseEntity<ResponseDTO> VerificarCodigo(@Valid @RequestBody CodigoDTO codigoDTO, HttpServletRequest request){
      authService.VerificarCodigo(codigoDTO);
      return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual), "P-200", "Codigo verificado correctamente", request.getRequestURI()), HttpStatus.OK);
    }

    @PostMapping("change-password")
    public ResponseEntity<ResponseDTO> CambiarContraseña(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, HttpServletRequest request){
      authService.CambiarContraseña(resetPasswordDTO);
      return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual), "P-200", "Contraseña cambiada correctamente", request.getRequestURI()), HttpStatus.OK);
    }


    /*
   @GetMapping("logout")
    public ResponseEntity<String> CerrarSesion(){
     return new ResponseEntity<>("Usuario deslogeado", HttpStatus.OK);
    }

     */

}

