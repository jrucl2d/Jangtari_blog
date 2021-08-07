package com.yu.jangtari.security;

import com.yu.jangtari.security.jwt.JwtAuthenticationFilter;
import com.yu.jangtari.security.jwt.SkipPathRequestMatcher;
import com.yu.jangtari.security.login.LoginFailureHandler;
import com.yu.jangtari.security.login.LoginFilter;
import com.yu.jangtari.security.login.LoginSuccessHandler;
import com.yu.jangtari.security.logout.CustomLogoutFilter;
import com.yu.jangtari.security.logout.CustomLogoutHandler;
import com.yu.jangtari.security.logout.CustomLogoutSuccessHandler;
import com.yu.jangtari.util.CookieUtil;
import com.yu.jangtari.util.JwtUtil;
import com.yu.jangtari.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * openPaths를 제외하고는 모두 401 Unauthorized 리턴
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;

    private final List<String> openPaths = Arrays.asList(
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/h2/**",
        "/login",
        "/logout",
        "/join"
    );

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .formLogin().disable()
            .httpBasic().disable() // rest api 서버 구축시 필요 없음. 비 인증시 로그인폼 화면으로 리다이렉트 해주는 기능
            .csrf().disable() // 폼 로그인이 아니기 때문에 csrf 보안 설정도 필요 없음
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 안 함
            .and()
                .authorizeRequests()
                .antMatchers(openPaths.toArray(new String[0]))
                    .permitAll()
                .antMatchers("/admin/**")
                    .hasRole("ADMIN")
                .antMatchers("/user/**")
                    .hasAnyRole("ADMIN", "USER")
                .anyRequest()
                    .authenticated()
            .and()
                .addFilterBefore(logoutFilter(), org.springframework.security.web.authentication.logout.LogoutFilter.class)
                .addFilter(logInFilter()) // UsernamePasswordAuthenticationFilter 기반 유저 인증
                .addFilterAfter(jwtAuthenticationFilter(), LoginFilter.class) // BasicAuthenticationFilter 기반 유저 인가
        ;
    }

    private LoginFilter logInFilter() throws Exception {
        LoginFilter logInFilter = new LoginFilter(authenticationManager());
        logInFilter.setFilterProcessesUrl("/login");
        logInFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(cookieUtil));
        logInFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        return logInFilter;
    }
    private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(), cookieUtil, jwtUtil, new SkipPathRequestMatcher(openPaths));
    }
    private CustomLogoutFilter logoutFilter() {
        CustomLogoutFilter customLogoutFilter = new CustomLogoutFilter(new CustomLogoutSuccessHandler(), new CustomLogoutHandler(cookieUtil));
        customLogoutFilter.setFilterProcessesUrl("/logout");
        return customLogoutFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}