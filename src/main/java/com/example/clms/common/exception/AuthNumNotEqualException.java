package com.example.clms.common.exception;

public class AuthNumNotEqualException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthNumNotEqualException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return errorCode.getStatus();
    }
}
