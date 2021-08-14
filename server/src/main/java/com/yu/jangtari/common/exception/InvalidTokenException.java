package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;

public class InvalidTokenException extends BusinessException
{
    public InvalidTokenException() {
        super("tmp");
        this.errorCode = ErrorCode.INVALID_TOKEN_ERROR;
    }
}
