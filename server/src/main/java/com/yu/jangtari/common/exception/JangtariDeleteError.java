package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;

public class JangtariDeleteError extends BusinessException
{
    public JangtariDeleteError() {
        super("tmp");
        this.errorCode = ErrorCode.JANGTARI_DELETE_ERROR;
    }
}
