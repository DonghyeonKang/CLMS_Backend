package com.example.clms.common.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public RefreshTokenNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return errorCode.getStatus();
    }
}
