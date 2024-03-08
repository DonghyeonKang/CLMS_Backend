package com.example.clms.common.exception;

public class UserNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public int getStatus() {
        return errorCode.getStatus();
    }
}
