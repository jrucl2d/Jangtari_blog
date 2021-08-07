package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;

public class NoSuchCategoryException extends BusinessException
{
    public NoSuchCategoryException() {
        this.errorCode = ErrorCode.CATEGORY_NOT_FOUND;
    }
}
