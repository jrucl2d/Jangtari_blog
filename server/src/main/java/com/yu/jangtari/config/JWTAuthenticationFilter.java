package com.yu.jangtari.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.jangtari.common.ErrorCode;
import com.yu.jangtari.common.ErrorResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// /login으로 post 방식의 {username, password} 요청시 동작하는 UsernamePasswordAuthenticationFilter를 설정
@RequiredArgsConstructor
@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final CookieUtil cookieUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginForm loginForm = objectMapper.readValue(request.getInputStream(), LoginForm.class);
            logger.info("** LOGIN : " + loginForm.getUsername());
            token = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
        } catch (IOException e) {
            log.error("IOException Occurred while login process");
        }
        return authenticationManager.authenticate(token); // UserDetailsService의 loadUserByUsername() 실행
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("성공");
        super.successfulAuthentication(request, response, chain, authResult);
    }

    // 토큰 생성, 쿠키에 넣어주는 기능 구현 필요
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.error("Handle LogIn Exception : ", failed);
        final ErrorCode errorCode = ErrorCode.MEMBER_NOT_FOUND;
        final ErrorResponse errorResponse = ErrorResponse.builder()
                                        .status(errorCode.getStatus())
                                        .code(errorCode.getCode())
                                        .message(errorCode.getMessage())
                                        .build();
        ObjectMapper objectMapper = new ObjectMapper();
        final String responseJson = objectMapper.writeValueAsString(errorResponse);
        response.setStatus(errorCode.getStatus());
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(responseJson);
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

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class LoginForm {
        private String username;
        private String password;
    }
}
