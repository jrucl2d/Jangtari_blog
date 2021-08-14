package com.yu.jangtari.common.exception;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import lombok.Getter;

@Getter
public class GoogleDriveException extends BusinessException
{
    public GoogleDriveException() {
        super("tmp");
        this.errorCode = ErrorCode.GOOGLE_DRIVE_ERROR;
    }
}
