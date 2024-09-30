package com.api.reservavuelos.Exceptions;

public class EmailDonSendException extends RuntimeException{
    public EmailDonSendException() {
        super("El email no pudo ser enviado");
    }
}
