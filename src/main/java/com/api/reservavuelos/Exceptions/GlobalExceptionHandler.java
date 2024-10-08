package com.api.reservavuelos.Exceptions;

import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.Utils.DateFormatter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final DateFormatter dateFormatter;
    private final Date tiempoactual = new Date();

    @Autowired
    public GlobalExceptionHandler(DateFormatter dateFormatter){
        this.dateFormatter = dateFormatter;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO> handleBadCredentialsException(HttpServletRequest request, BadCredentialsException exception){
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-400", "La contraseña no coincide", request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleNoHandlerFoundException(HttpServletRequest request, UrlNotFoundException exception){
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-404", "No se encontro la ruta", request.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception){
        Map<String, String> mapErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String code = error.getCode();
            String message = error.getDefaultMessage();
            mapErrors.put(code, message);
        });
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-400", mapErrors.toString(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseDTO> handleJwtTokenExpiredException(HttpServletRequest request, ExpiredJwtException exception){
    return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-401", "El token ha Expirado", request.getRequestURI()), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException exception){
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-400", exception.getMessage(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<ResponseDTO> handleJwtTokenMalformedException(HttpServletRequest request, MalformedJwtException exception){
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-401", "El token es invalido", request.getRequestURI()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseDTO> handleIOException(HttpServletRequest request, IOException exception){
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-500", exception.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleAuthenticationCredentialsNotFoundException(HttpServletRequest request , AuthenticationCredentialsNotFoundException exception){
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-401", exception.getMessage(), request.getRequestURI()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ResponseDTO> handleMissingServletRequestPartException(HttpServletRequest request, MissingServletRequestPartException exception) {
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-400", "Falta el parametro: " + exception.getRequestPartName(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleUserNotFoundException(HttpServletRequest request, UserNotFoundException exception) {
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-404", exception.getMessage(), request.getRequestURI()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<ResponseDTO> handleUserAlreadyRegisterException(HttpServletRequest request, UserAlreadyRegisterException exception) {
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"p-500", exception.getMessage(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailDontSendException.class)
    public ResponseEntity<ResponseDTO> handleEmailDonSendException(HttpServletRequest request, EmailDontSendException exception) {
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-500", exception.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO> handleHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-400", "El request no puede estar vacio, debe contener los datos requeridos en el body", request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CodeNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleCodeNotFoundException(HttpServletRequest request, CodeNotFoundException exception) {
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual),"P-404", exception.getMessage(), request.getRequestURI()), HttpStatus.NOT_FOUND);
    }


}

