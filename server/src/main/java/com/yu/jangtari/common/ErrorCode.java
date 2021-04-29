package com.yu.jangtari.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "yu001", "Invalid Input Value"),
    ACCESS_DENIED(403, "yu002", "Access Denied"),
    INTERNAL_SERVER_ERROR(500, "yu003", "Internal Server Error"),
    METHOD_NOT_SUPPORTED(405, "yu004", "HTTP Method Not Supported");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
