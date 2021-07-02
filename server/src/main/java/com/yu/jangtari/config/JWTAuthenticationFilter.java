package com.yu.jangtari.config;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// OncePerRequestFilter -> 한 요청 당 한 번만 JWT 토큰 검사
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final RedisUtil redisUtil;
    private final JWTUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final Cookie accessCookie = cookieUtil.getCookie(request, CookieUtil.ACCESS_COOKIE_NAME);

        if (accessCookie == null) {
            final Cookie refreshCookie = cookieUtil.getCookie(request, CookieUtil.REFRESH_COOKIE_NAME);
            if (refreshCookie == null) {

            }
        }
        else {

        }
        String accessToken = null;
        String refreshToken = null;
        String username = null;
        String redisUsername = null;
        try {
            if (accessCookie != null) {
                // 쿠키에 access token 있다면
                accessToken = accessCookie.getValue();
                username = jwtUtil.getUsernameFromJWT(accessToken);
                if (jwtUtil.validateToken(accessToken)) {
                    Authentication authentication = jwtUtil.getAuthentication(username);
                    SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 Authentication 객체를 저장
                    filterChain.doFilter(request, response);
                }
            }
        } catch (ExpiredJwtException e) {
            final Cookie refreshCookie = cookieUtil.getCookie(request, CookieUtil.REFRESH_COOKIE_NAME);
            if (refreshCookie != null) {
                refreshToken = refreshCookie.getValue();
            }
        } catch (Exception e) {

        }
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        final Cookie accessCookie = cookieUtil.getCookie(request, jwtTokenProvider.ACCESS_TOKEN_STRING);
//
//        String username = null;
//        String accessJwt = null;
//        String refreshJwt = null;
//        String redisUsername = null;
//
//        try{
//            // 쿠키에 accessToken이 만료되지 않고 남아있다면
//            if(accessCookie != null){
//                accessJwt = accessCookie.getValue();
//                username = jwtTokenProvider.getUserPk(accessJwt);
//            }
//            if(username!=null){
//                if(jwtTokenProvider.validateToken(accessJwt)){
//                    Authentication authentication = jwtTokenProvider.getAuthentication(accessJwt); // 토큰이 유효하다면 토큰으로부터 유저 정보를 가져옴
//                    SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext에 Authentication 객체를 저장
//                }
//            }
//        }catch (ExpiredJwtException e){
//            // accessToken이 만료되었다면
//            Cookie refreshToken = cookieUtil.getCookie(request,jwtTokenProvider.REFRESH_TOKEN_STRING);
//            if(refreshToken!=null){
//                refreshJwt = refreshToken.getValue();
//            }
//        }catch(Exception e){
//
//        }
//        try{
//            if(refreshJwt != null){
//                redisUsername = redisUtil.getData(refreshJwt);
//
//                // redis에 저장된 이름과 refresh 토큰으로부터 찾은 username이 같다면
//                if(redisUsername.equals(jwtTokenProvider.getUserPk(refreshJwt))){
//                    Authentication authentication = jwtTokenProvider.getAuthentication(refreshJwt); // Refresh token으로 확인했으므로 로그인 처리
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    String newAccessToken = jwtTokenProvider.createAccessToken(redisUsername); // 만료된 Access token을 재발급
//
//                    Cookie newAccessCookie = cookieUtil.createCookie(jwtTokenProvider.ACCESS_TOKEN_STRING, newAccessToken);
//                    response.addCookie(newAccessCookie);
//                }
//            }
//        }catch(ExpiredJwtException e){
//        }
//        chain.doFilter(request, response);
//    }
}
