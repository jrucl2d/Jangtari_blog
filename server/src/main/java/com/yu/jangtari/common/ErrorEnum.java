package com.yu.jangtari.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    INVALID_PATH_PARAMETER("code01", "INVALID_PATH_PARAMETER"),
    INVALID_QUERY_PARAMETER("code02", "INVALID_QUERY_PARAMETER"),
    INVALID_REQUEST_BODY("code03", "INVALID_PATH_PARAMETER");

    String errorCode;
    String errorTitle;
}
