package com.api.reservavuelos.Exceptions;

public class EmailDontSendException extends RuntimeException{
    public EmailDontSendException() {
        super("El email no pudo ser enviado");
    }
}
