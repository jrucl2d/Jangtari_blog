package com.yu.jangtari.security.login;

import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.exception.ErrorResponse;
import com.yu.jangtari.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.LOGIN_ERROR);
        ResponseUtil.doResponse(response, errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
