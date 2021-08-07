package com.yu.jangtari.security;

import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.exception.ErrorResponse;
import com.yu.jangtari.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 모든 인증되지 않은 요청을 처리 -> 401
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint
{
    @Override
    public void commence(
        HttpServletRequest request
        , HttpServletResponse response
        , AuthenticationException authException) throws IOException, ServletException
    {
        log.error("UnAuthorized Error : ", authException);
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED_ERROR);
        ResponseUtil.doResponse(response, errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
