package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchCommentException extends BusinessException
{
    public NoSuchCommentException() {
        super("tmp");
        this.errorCode = ErrorCode.COMMENT_NOT_FOUND; // error code add needed
    }
}
