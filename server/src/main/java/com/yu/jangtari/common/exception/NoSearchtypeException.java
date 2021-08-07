package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;

public class NoSearchtypeException extends BusinessException
{
    public NoSearchtypeException() {
        this.errorCode = ErrorCode.SEARCH_TYPE_ERROR;
    }
}
