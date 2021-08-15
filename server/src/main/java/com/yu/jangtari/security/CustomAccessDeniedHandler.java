package com.yu.jangtari.security;

import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.exception.ErrorResponse;
import com.yu.jangtari.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 인증은 됐는데(token은 있는데) 권한이 없는 경우
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler
{
    @Override
    public void handle(
        HttpServletRequest request
        , HttpServletResponse response
        , AccessDeniedException accessDeniedException) throws IOException, ServletException
    {
        log.error("Access Denied Error : ", accessDeniedException);
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ACCESS_DENIED_ERROR);
        ResponseUtil.doResponse(response, errorResponse, HttpStatus.FORBIDDEN);
    }
}
