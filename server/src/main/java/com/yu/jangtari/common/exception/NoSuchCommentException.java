package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchCommentException extends BusinessException {
    public NoSuchCommentException() {
        this.errorCode = ErrorCode.COMMENT_NOT_FOUND; // error code add needed
    }
}
