package com.yu.jangtari.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    // =================================================================================================================
    // Validation 과정에서 FieldError 발생시
    // =================================================================================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleMethodArgumentNotValidException (MethodArgumentNotValidException e) {
        log.error("Handle method argument not valid exception : ", e);
        return new ErrorResponse(e.getFieldErrors());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleIllegalArgumentException (IllegalArgumentException e) {
        log.error("Handle illegal argument not valid exception : ", e);
        return new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE);
    }

    // =================================================================================================================
    // 비즈니스 로직과 관련된 에러 발생시
    // =================================================================================================================
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleBusinessException (BusinessException e) {
        log.error("Handle Business Exception : ", e);
        return new ErrorResponse(e.getErrorCode(), e.getMessage());
    }
}
