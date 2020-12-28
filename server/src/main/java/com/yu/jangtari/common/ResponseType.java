package com.yu.jangtari.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseType<T> {
    private ErrorType error;
    private T result;

    public static ResponseType<String> OK() {
        return new ResponseType<>(null, "OK");
    }
}
