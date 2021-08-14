package com.yu.jangtari.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    protected ErrorCode errorCode;
    public BusinessException(String msg) {
        super(msg);
        this.errorCode = null;
    }
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public BusinessException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
