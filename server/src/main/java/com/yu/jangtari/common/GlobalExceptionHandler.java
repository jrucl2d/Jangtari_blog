package com.yu.jangtari.common;

import com.yu.jangtari.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

    /*
    * java.validatin.Valid 혹은 @Validated에 의해 binding error가 발생할 경우.
    * HttpMessageConverter에서 등록한 HttpMessageConverter가 binding에 실패할 경우.
    * @RequestBody, @RequestPart 어노테이션에서 발생
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException (MethodArgumentNotValidException e) {
        log.error("Handle method argument not valid exception : ", e);
        final ErrorResponse errorResponse = buildError(ErrorCode.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 지원하지 않는 HTTP 메소드 호출시 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException (HttpRequestMethodNotSupportedException e) {
        log.error("Handle HTTP Method Not Supported Exception : ", e);
        final ErrorResponse errorResponse = buildError(ErrorCode.METHOD_NOT_SUPPORTED);
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 접근 권한(ROLE)이 없는 경우 발생
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException (AccessDeniedException e) {
        log.error("Handle Access Denied Exception : ", e);
        final ErrorResponse errorResponse = buildError(ErrorCode.ACCESS_DENIED);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // 비즈니스 로직 진행 중 발생한 예외 처리
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException (BusinessException e) {
        log.error("Handle Business Exception : ", e);
        final ErrorResponse errorResponse = buildError(e.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 파라미터 검증에 의한 FieldError를 갖지 않는 에러 생성
    private ErrorResponse buildError(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .build();
    }

    // 파라미터 검증에 의한 FieldError를 갖는 에러 생성
    private ErrorResponse buildFieldErrors(ErrorCode errorCode, List<ErrorResponse.FieldError> fieldErrors) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .status(errorCode.getStatus())
                .message(errorCode.getMessage())
                .errors(fieldErrors)
                .build();
    }
    /*
     * 컨트롤러에서 @Valid를 사용해 파라미터 검증 후 에러가 났을 때 BindingResult로 바인딩 된 결과를 받아옴
     * 내부의 FieldError를 ErrorResponse.FieldError의 커스텀 객체로 변환하여 리턴
    */
    private List<ErrorResponse.FieldError> getFieldErrors(BindingResult bindingResult) {
        final List<FieldError> errors = bindingResult.getFieldErrors();
        return errors.parallelStream()
                .map(error -> ErrorResponse.FieldError
                        .builder()
                        .reason(error.getDefaultMessage())
                        .field(error.getField())
                        .value((String) error.getRejectedValue())
                        .build())
                .collect(Collectors.toList());
    }
}
