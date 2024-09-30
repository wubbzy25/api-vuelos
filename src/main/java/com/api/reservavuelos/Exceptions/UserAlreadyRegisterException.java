package com.api.reservavuelos.Exceptions;

public class UserAlreadyRegisterException extends RuntimeException{

    public UserAlreadyRegisterException() {
        super("El usuario ya se encuentra registrado en la aplicacion");
    }
}
