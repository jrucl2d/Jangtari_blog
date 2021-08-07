package com.yu.jangtari.security.logOut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.exception.GlobalExceptionHandler;
import com.yu.jangtari.common.exception.InvalidTokenException;
import com.yu.jangtari.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * '/logout'으로 오는 요청에서 accessCookie와 refreshCookie, SecurityContext 안의 Authentication을 제거
 */
@Slf4j
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler
{
    private final CookieUtil cookieUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        try {
            log.info("** 로그아웃합니다.");
            Cookie accessCookie = cookieUtil.getLogoutCookie(cookieUtil.getAccessCookie(request));
            Cookie refreshCookie = cookieUtil.getLogoutCookie(cookieUtil.getRefreshCookie(request));
            SecurityContextHolder.getContext().setAuthentication(null);
            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);
        } catch (InvalidTokenException e) {
            // Cookie가 없는 경우 애초에 로그아웃이 불가능
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                final String responseJson = objectMapper.writeValueAsString(GlobalExceptionHandler.buildError(ErrorCode.INVALID_TOKEN_ERROR));
                response.setStatus(ErrorCode.INVALID_TOKEN_ERROR.getStatus());
                response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
                response.getWriter().write(responseJson);
            } catch (Exception ee) {
                log.error("로그아웃 도중 에러가 발생했습니다.", ee);
            }
        }
    }
}
