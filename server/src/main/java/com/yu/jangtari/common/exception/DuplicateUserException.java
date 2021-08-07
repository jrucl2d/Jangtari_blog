package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;

public class DuplicateUserException extends BusinessException
{
    public DuplicateUserException() {
        this.errorCode = ErrorCode.DUPLICATE_USER_ERROR;
    }
}
