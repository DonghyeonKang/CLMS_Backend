package com.example.clms.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private String status;
    private T data;
    private String message;

    public static ApiResponse<?> createSuccessWithNoContent() {
        return new ApiResponse<>(SUCCESS_STATUS, null, null);
    }
    public static <T> ApiResponse<T> createSuccessWithContent(T data) {
        return new ApiResponse<>(SUCCESS_STATUS, data, null);
    }

    public static ApiResponse<?> userNotFoundException(String message) {
        return new ApiResponse<>(ERROR_STATUS, null, message);
    }

    public static ApiResponse<?> duplicateEmailException(String message) {
        return new ApiResponse<>(FAIL_STATUS, null, message);
    }

    public static ApiResponse<?> authNumNotEqualException(String message) {
        return new ApiResponse<>(FAIL_STATUS, null, message);
    }

    public static ApiResponse<?> refreshTokenNotFoundException(String message) {
        return new ApiResponse<>(FAIL_STATUS, null, message);
    }

    public static ApiResponse<?> memberAuthenticationException(String message) {
        return new ApiResponse<>(FAIL_STATUS, null, message);
    }

    public static ApiResponse<?> methodArgumentNotValidException(String message) {
        return new ApiResponse<>(ERROR_STATUS, null, message);
    }

    public static ApiResponse<?> emptyDataAccessException(String message) {
        return new ApiResponse<>(ERROR_STATUS, null, message);
    }

    public static ApiResponse<?> argumentNotValidException(String message) {
        return new ApiResponse<>(ERROR_STATUS, null, message);
    }

    public static ApiResponse<?> duplicatedRequestException(String message) {
        return new ApiResponse<>(FAIL_STATUS, null, message);
    }

    private ApiResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}