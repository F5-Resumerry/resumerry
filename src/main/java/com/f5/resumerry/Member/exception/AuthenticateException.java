package com.f5.resumerry.Member.exception;

public class AuthenticateException extends RuntimeException {

    public AuthenticateException(String message) {
        super(message);
    }

    public AuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }
}
