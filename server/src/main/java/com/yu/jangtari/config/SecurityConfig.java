package com.yu.jangtari.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    public SecurityConfig(JWTTokenProvider jwtTokenProvider, RedisUtil redisUtil) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisUtil = redisUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // rest api만을 고려하여 기본 설정 해제
                .csrf().disable() // JWT 로그인에는 csurf 인증 불필요
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // rest api : stateless. cookie에 세션 저장하지 않음
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/**").permitAll()
                .and()
                .addFilterBefore(new JWTAuthenticationFilter(jwtTokenProvider, redisUtil), // 사용자 인증 처리 필터 전에 커스텀 필터를 통해 JWT 토큰을 가지고 사용자 정보를 넣어준다.
                        UsernamePasswordAuthenticationFilter.class);
    }
}
