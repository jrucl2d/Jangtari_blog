package com.yu.jangtari.security.logout;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class CustomLogoutFilter extends LogoutFilter
{
    public CustomLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers)
    {
        super(logoutSuccessHandler, handlers);
    }
}
