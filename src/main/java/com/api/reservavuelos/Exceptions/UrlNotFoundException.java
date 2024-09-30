package com.api.reservavuelos.Exceptions;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException() {
        super("La url ingresada no existe :(");
    }
}
