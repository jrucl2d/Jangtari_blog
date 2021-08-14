package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;

public class NoSuchPostException extends BusinessException
{
    public NoSuchPostException() {
        super("tmp");
        this.errorCode = ErrorCode.POST_NOT_FOUND;
    }
}
