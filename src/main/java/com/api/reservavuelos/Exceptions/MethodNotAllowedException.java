package com.api.reservavuelos.Exceptions;

public class MethodNotAllowedException extends RuntimeException {

    public MethodNotAllowedException() {
        super("El metodo HTTP ingresado no es permitido para esta url");
    }
}
