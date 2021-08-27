package com.yu.jangtari.util;

import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    private AuthUtil() {
        throw new IllegalStateException("Utility Class");
    }

    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || (!authentication.isAuthenticated()))
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND_ERROR);

        return (String) authentication.getPrincipal();
    }
}
