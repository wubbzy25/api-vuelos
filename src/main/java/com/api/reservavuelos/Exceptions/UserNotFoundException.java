package com.api.reservavuelos.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{

   public UserNotFoundException() {
       super("El usuario no se encuentra registrado en la aplicacion");
   }
}
