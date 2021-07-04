package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;

public class DuplicateUserException extends BusinessException {
    public DuplicateUserException() {
        this.errorCode = ErrorCode.DUPLICATE_USER_ERROR;
    }
}
