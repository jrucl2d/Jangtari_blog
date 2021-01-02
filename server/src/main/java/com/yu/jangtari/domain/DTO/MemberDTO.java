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
}