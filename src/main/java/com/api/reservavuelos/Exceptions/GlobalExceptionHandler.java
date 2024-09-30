package com.api.reservavuelos.Exceptions;

import com.api.reservavuelos.DTO.ResponseExceptionDTO;
import com.api.reservavuelos.Utils.DateFormatter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private DateFormatter dateFormatter;

    private Date tiempoactual = new Date();


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseExceptionDTO> handleBadCredentialsException(HttpServletRequest request, BadCredentialsException exception){
        return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"P-400", "La contraseña no coincide", request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ResponseExceptionDTO> handleNoHandlerFoundException(HttpServletRequest request, UrlNotFoundException exception){
        return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"P-404", "No se encontro la ruta", request.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception){
        Map<String, String> mapErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String code = error.getCode();
            String message = error.getDefaultMessage();
            mapErrors.put(code, message);
        });
        return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"P-400", mapErrors.toString(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseExceptionDTO> handleJwtTokenExpiredException(HttpServletRequest request, ExpiredJwtException exception){
    return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"P-401", "El token ha Expirado", request.getRequestURI()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseExceptionDTO> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException exception){
        return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"P-401", "No estas autenticado para realizar esta accion", request.getRequestURI()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<ResponseExceptionDTO> handleJwtTokenMalformedException(HttpServletRequest request, MalformedJwtException exception){
        return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"P-401", "El token es invalido", request.getRequestURI()), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ResponseExceptionDTO> handleAuthenticationCredentialsNotFoundException(HttpServletRequest request , AuthenticationCredentialsNotFoundException exception){
        return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"P-401", exception.getMessage(), request.getRequestURI()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseExceptionDTO> handleUserNotFoundException(HttpServletRequest request, UserNotFoundException exception) {
        return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"P-404", exception.getMessage(), request.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<ResponseExceptionDTO> handleUserAlreadyRegisterException(HttpServletRequest request, UserAlreadyRegisterException exception) {
        return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"p-500", exception.getMessage(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailDonSendException.class)
    public ResponseEntity<ResponseExceptionDTO> handleEmailDonSendException(HttpServletRequest request, EmailDonSendException exception) {
        return new ResponseEntity<>(new ResponseExceptionDTO(dateFormatter.formatearFecha(tiempoactual),"P-500", exception.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

