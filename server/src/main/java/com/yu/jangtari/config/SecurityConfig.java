package com.yu.jangtari.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CookieUtil cookieUtil;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 폼 로그인이 아니기 때문에 csrf 보안 설정도 필요 없음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 안 함
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), cookieUtil)) // UsernamePasswordAuthenticationFilter 기반 유저 인증
//                .addFilter(new JWTAuthorizationFilter(authenticationManager())) // 유저 인가
                .formLogin().disable()
                .httpBasic().disable() // rest api 서버 구축시 필요 없음. 비 인증시 로그인폼 화면으로 리다이렉트 해주는 기능
                .authorizeRequests() // 다음의 request에 대한 인가 설정
                    .antMatchers("/amdin/**").hasAnyRole("ADMIN")
                    .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                    .anyRequest().permitAll()
                ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}