package com.yu.jangtari.domain.DTO;


import lombok.*;

public class MemberDTO {

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Add {
        private String username;
        private String nickname;
        private String password;
    }
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {
        private String username;
        private String password;
    }
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Token{
        private String accessToken;
        private String refreshToken;
    }
}
