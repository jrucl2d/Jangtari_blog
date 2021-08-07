package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;

public class NoMasterException extends BusinessException
{
    public NoMasterException() {
        this.errorCode = ErrorCode.ACCESS_DENIED;
    }
}
