package com.yu.jangtari.config;

import com.yu.jangtari.common.JwtToken;
import com.yu.jangtari.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Provider;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CookieUtil cookieUtil;
    private final Provider<JwtToken> jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 폼 로그인이 아니기 때문에 csrf 보안 설정도 필요 없음
                .csrf().ignoringAntMatchers("/h2/**").and().headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 안 함
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), cookieUtil, jwtTokenProvider)) // UsernamePasswordAuthenticationFilter 기반 유저 인증
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), cookieUtil, jwtTokenProvider)) // BasicAuthenticationFilter 기반 유저 인가
                .formLogin().disable()
                .httpBasic().disable() // rest api 서버 구축시 필요 없음. 비 인증시 로그인폼 화면으로 리다이렉트 해주는 기능
                .authorizeRequests() // 다음의 request에 대한 인가 설정
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                    .anyRequest().permitAll()
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}