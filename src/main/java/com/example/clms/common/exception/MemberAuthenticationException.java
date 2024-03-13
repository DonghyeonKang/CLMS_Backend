package com.example.clms.common.exception;

public class MemberAuthenticationException extends RuntimeException {
    private final ErrorCode errorCode;

    public MemberAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return errorCode.getStatus();
    }
}
