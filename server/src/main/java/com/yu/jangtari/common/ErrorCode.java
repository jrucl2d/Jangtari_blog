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
    METHOD_NOT_SUPPORTED(405, "yu004", "HTTP Method Not Supported"),

    // Custom
    MEMBER_NOT_FOUND(404, "yu005", "No Such Member"),
    COMMENT_NOT_FOUND(404, "yu006", "No Such Comment"),
    CATEGORY_NOT_FOUND(404, "yu007", "No Such Category"),
    PICTURE_NOT_FOUND(404, "yu008", "No Such Picture"),
    HASHTAG_NOT_FOUND(404, "yu009", "No Such Hashtag"),
    FILE_ERROR(500, "yu010", "File Task Error"),
    GOOGLE_DRIVE_ERROR(500, "yu011", "Google Dirve Error");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
