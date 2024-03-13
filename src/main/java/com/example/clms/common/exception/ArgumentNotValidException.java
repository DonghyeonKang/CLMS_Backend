package com.example.clms.common.exception;

public class ArgumentNotValidException extends RuntimeException {

    private final ErrorCode errorCode;

    public ArgumentNotValidException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return errorCode.getStatus();
    }
}
