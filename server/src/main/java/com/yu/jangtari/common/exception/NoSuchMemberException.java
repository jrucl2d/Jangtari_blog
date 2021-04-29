package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;

public class NoSuchMemberException extends BusinessException{
    private ErrorCode errorCode = ErrorCode.MEMBER_NOT_FOUND;
}
