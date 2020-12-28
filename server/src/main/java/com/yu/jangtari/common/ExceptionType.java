package com.yu.jangtari.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionType extends Exception {
    private String message;
    private String detail;

    public ExceptionType(String message, String detail){
        super();
        this.message = message;
        this.detail = detail;
    }
}
