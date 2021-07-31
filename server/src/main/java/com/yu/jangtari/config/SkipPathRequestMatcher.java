package com.yu.jangtari.config;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link JWTAuthorizationFilter}에서 권한 체크가 필요 없는 path를 걸러내는 작업
 */
public class SkipPathRequestMatcher implements RequestMatcher
{
    private final OrRequestMatcher skipMatcher;

    public SkipPathRequestMatcher(List<String> skipPaths)
    {
        if (CollectionUtils.isEmpty(skipPaths))
            throw new IllegalArgumentException("skipPaths cannot be empty");

        List<RequestMatcher> requestMatchers = skipPaths.stream()
            .map(AntPathRequestMatcher::new)
            .collect(Collectors.toList());
        this.skipMatcher = new OrRequestMatcher(requestMatchers);
    }

    @Override
    public boolean matches(HttpServletRequest request)
    {
        return skipMatcher.matches(request);
    }
}