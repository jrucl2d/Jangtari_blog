package com.yu.jangtari.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    protected ErrorCode errorCode;
}
