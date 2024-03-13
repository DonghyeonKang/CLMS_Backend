package com.example.clms.common.exception;

public enum ErrorCode {
    USER_NOT_FOUND(400, "해당 유저를 찾을 수 없습니다."),
    DUPLICATED_EMAIL(400, "이미 존재하는 E-mail 입니다."),
    DUPLICATED_NICKNAME(400, "이미 존재하는 Nickname 입니다."),
    MEMBER_ERROR(400, "아이디나 비밀번호가 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(400, "Refresh Token 이 존재하지 않습니다."),
    FAIL_VERIFY(400, "인증 번호가 틀렸습니다."),
    NOT_VERIFIED_PHONE(400, "인증되지 않은 번호입니다."),
    NOT_VALID_ARGUMENT(400, "잘못된 요청 데이터입니다."),
    NOT_SEND_MESSAGE(400, "인증 번호가 전송되지 않았습니다."),
    REFRESH_NOT_EXIST(401, "인증 정보가 유효하지 않습니다."),
    ACCESS_NOT_EXIST(401, "인증 정보가 유효하지 않습니다."),
    EXPIRED_TOKEN(401, "인증 정보가 유효하지 않습니다."),
    INVALID_CLAIM(401, "인증 정보가 유효하지 않습니다."),
    INVALID_SIGNATURE(401, "인증 정보가 유효하지 않습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
