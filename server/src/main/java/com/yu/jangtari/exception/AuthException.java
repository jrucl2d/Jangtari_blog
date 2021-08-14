package com.yu.jangtari.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthException extends AuthenticationException {
    private ErrorCode errorCode;

    public AuthException(String msg) {
        super(msg);
    }

    public AuthException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
