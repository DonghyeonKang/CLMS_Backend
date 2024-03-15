package com.example.clms.common.exception;

public class EmptyDataAccessException extends RuntimeException {
    private final ErrorCode errorCode;

    public EmptyDataAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return errorCode.getStatus();
    }
}
