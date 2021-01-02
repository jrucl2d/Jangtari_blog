package com.yu.jangtari.config;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;

    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String accessTokenUsername = null;

        // accessToken이 존재한다면 검사 없이 무조건 인증(대신 expire 시간이 짧음)
        try{
            if(accessToken != null){
                accessTokenUsername = jwtTokenProvider.getUserPk(accessToken);
            }
            if(accessTokenUsername != null){
                if (jwtTokenProvider.validateToken(accessToken)){
                    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken); // 토큰이 유효하다면 토큰으로부터 유저 정보를 가져옴
                    SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 Authentication 객체를 저장
                }
            }
        } catch (ExpiredJwtException e){
            // 만약 accessToken이 만료되었다면
            try{
                String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

                if(refreshToken != null){
                    String refreshTokenUsername = redisUtil.getData(refreshToken);

                    if(refreshTokenUsername.equals(jwtTokenProvider.getUserPk(refreshToken))) {
                        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken); // Refresh token으로 확인햇으므로 로그인 처리
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String newAccessToken = jwtTokenProvider.createAccessToken(refreshTokenUsername); // 만료된 Access token을 재발급
                        response.addHeader("newAccessToken", newAccessToken); // 새로운 Access token을 헤더에 담아 제공
                    }
                }
            } catch (Exception e2){
                // Refresh token도 만료되었다면
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }
        chain.doFilter(request, response);
    }
}
