package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;

public class JangtariDeleteError extends BusinessException{
    public JangtariDeleteError() {
        this.errorCode = ErrorCode.JANGTARI_DELETE_ERROR;
    }
}
