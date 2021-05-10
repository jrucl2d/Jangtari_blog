package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;

public class NoSuchPostException extends BusinessException{
    public NoSuchPostException() {
        this.errorCode = ErrorCode.POST_NOT_FOUND;
    }
}
