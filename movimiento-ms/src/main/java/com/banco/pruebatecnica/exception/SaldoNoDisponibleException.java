package com.banco.pruebatecnica.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SaldoNoDisponibleException extends RuntimeException {
    public SaldoNoDisponibleException(String message) {
        super(message);
    }
}