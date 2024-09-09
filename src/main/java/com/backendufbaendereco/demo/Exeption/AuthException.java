package com.backendufbaendereco.demo.Exeption;

public class AuthException extends  RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}
