package com.yu.jangtari.config;

//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //    private final JWTTokenProvider jwtTokenProvider;
//    private final RedisUtil redisUtil;
//    private final CookieUtil cookieUtil;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable() // rest api 서버 구축시 필요 없음. 비 인증시 로그인폼 화면으로 리다이렉트 해주는 기능
                .csrf().disable() // 폼 로그인이 아니기 때문에 csrf 보안 설정도 필요 없음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 안 함
                .and()
                .authorizeRequests() // 다음의 request에 대한 인가 설정
                    .antMatchers("/amdin/**").hasAnyRole("ADMIN")
                    .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                    .anyRequest().permitAll()
        //          .addFilterBefore(new JWTAuthenticationFilter(jwtTokenProvider, redisUtil, cookieUtil), // 사용자 인증 처리 필터 전에 커스텀 필터를 통해 JWT 토큰을 가지고 사용자 정보를 넣어준다.
        //           UsernamePasswordAuthenticationFilter.class)
                ;
    }

    // AuthenticationManager를 bean으로 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}