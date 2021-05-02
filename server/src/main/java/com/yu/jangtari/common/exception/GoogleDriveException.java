package com.yu.jangtari.common.exception;

import com.yu.jangtari.common.ErrorCode;
import lombok.Getter;

@Getter
public class GoogleDriveException extends BusinessException{
    public GoogleDriveException() {
        this.errorCode = ErrorCode.GOOGLE_DRIVE_ERROR;
    }
}
