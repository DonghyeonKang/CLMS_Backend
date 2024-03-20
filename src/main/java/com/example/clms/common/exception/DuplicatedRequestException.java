package com.example.clms.common.exception;

public class DuplicatedRequestException extends RuntimeException {
    private final ErrorCode errorCode;

    public DuplicatedRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return errorCode.getStatus();
    }
}
