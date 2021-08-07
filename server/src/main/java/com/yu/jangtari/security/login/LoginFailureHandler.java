package com.yu.jangtari.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request
        , HttpServletResponse response
        , AuthenticationException exception) throws IOException, ServletException {

        log.error("** 로그인 실패 : ", exception);

        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(new ErrorResponse(ErrorCode.LOGIN_ERROR));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(message);
    }
}
