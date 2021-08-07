package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchMemberException extends BusinessException
{
    public NoSuchMemberException() {
        this.errorCode = ErrorCode.MEMBER_NOT_FOUND;
    }
}
