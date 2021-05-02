package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;

public class NoSuchCategoryException extends BusinessException{
    public NoSuchCategoryException() {
        this.errorCode = ErrorCode.CATEGORY_NOT_FOUND;
    }
}
