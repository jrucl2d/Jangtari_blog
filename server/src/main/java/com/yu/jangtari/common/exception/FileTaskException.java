package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;
import lombok.Getter;

@Getter
public class FileTaskException extends BusinessException{
    public FileTaskException() {
        this.errorCode = ErrorCode.FILE_ERROR;
    }
}
