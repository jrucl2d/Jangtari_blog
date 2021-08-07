package com.yu.jangtari.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuseonggeun
 * 프론트에게 동일한 Error Response를 전해주기 위한 POJP 객체
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private String code;
    private List<FieldErrorForm> errors;

    public ErrorResponse(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(List<FieldError> fieldErrors)
    {
        this(ErrorCode.INVALID_INPUT_VALUE);
        this.errors = fieldErrors.stream()
            .map(FieldErrorForm::of)
            .collect(Collectors.toList());
    }

    public ErrorResponse(ErrorCode errorCode, String message)
    {
        this(errorCode);
        this.message = message;
    }

    @Getter
    public static class FieldErrorForm {
        private final String field;
        private final String message;
        private final Object value;

        public static FieldErrorForm of(FieldError fieldError) {
            return FieldErrorForm.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .value(fieldError.getCode())
                .build();
        }

        @Builder
        private FieldErrorForm(String field, String message, Object value) {
            this.field = field;
            this.message = message;
            this.value = value;
        }
    }

}
