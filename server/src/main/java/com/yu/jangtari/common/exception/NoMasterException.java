package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;

public class NoMasterException extends BusinessException {
    public NoMasterException() {
        this.errorCode = ErrorCode.ACCESS_DENIED;
    }
}
