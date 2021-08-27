package com.yu.jangtari.util;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    private AuthUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static Long getMemberId() {
        return getJwtInfo().getMemberId();
    }

    public static String getUsername() {
        return getJwtInfo().getUsername();
    }

    private static JwtInfo getJwtInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || (!authentication.isAuthenticated()))
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND_ERROR);

        return (JwtInfo) authentication.getPrincipal();
    }
}
