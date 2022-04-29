package com.f5.resumerry.exception;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException{

    private ErrorCode errorCode;

    private String field;

    public DuplicateException(String field, String message, ErrorCode errorCode){
        super(message);
        this.field = field;
        this.errorCode = errorCode;
    }
}