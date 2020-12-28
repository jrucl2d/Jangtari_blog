package com.yu.jangtari.common;

import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;
import java.io.StringWriter;

@Getter
@Setter
public class ErrorType {

    private String errorCode;
    private String message;
    private String detail;
    private String stackTrace;

    public ErrorType(ExceptionType e){
        this.errorCode = "Error00";
        this.message = e.getMessage();
        this.detail = e.getDetail();
        this.stackTrace = e.getStackTrace().toString();
    }

    public ErrorType(ErrorEnum error, String detail){
        this.errorCode = error.getErrorCode();
        this.message = error.getErrorTitle();
        this.detail = detail;
    }

    public ErrorType(String errorCode, String message, String detail){
        this.errorCode = errorCode;
        this.message = message;
        this.detail = detail;
    }

    public ErrorType(String errorCode, String message, String detail, Exception exception){
        this.errorCode = errorCode;
        this.message = message;
        this.detail = detail;
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        this.stackTrace = stringWriter.toString();
    }
}
