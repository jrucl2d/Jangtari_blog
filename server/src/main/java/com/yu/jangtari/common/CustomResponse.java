package com.yu.jangtari.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomResponse<T> {
    private ErrorResponse error;
    private T result;

    public static CustomResponse<String> OK() {
        return new CustomResponse<>(null, "OK");
    }
}
