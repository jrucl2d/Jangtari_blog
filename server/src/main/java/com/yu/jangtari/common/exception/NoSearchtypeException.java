package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;

public class NoSearchtypeException extends BusinessException {
    public NoSearchtypeException() {
        this.errorCode = ErrorCode.SEARCH_TYPE_ERROR;
    }
}
