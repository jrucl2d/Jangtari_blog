package com.yu.jangtari.domain.DTO;


import com.yu.jangtari.domain.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class MemberDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Get {
        private String username;
        private String nickname;
        private String introduce;
        private String picture;

        public Get(Member member) {
            this.username = member.getUsername();
            this.nickname = member.getNickname();
            this.introduce = member.getIntroduce();
            this.picture = member.getPicture();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Add {
        @NotBlank(message = "아이디가 빈칸이면 안 됩니다.")
        private String username;
        @NotBlank(message = "닉네임이 빈칸이면 안 됩니다.")
        private String nickname;
        @NotBlank(message = "비밀번호가 빈칸이면 안 됩니다.")
        private String password;

        @Builder
        public Add(String username, String nickname, String password) {
            this.username = username;
            this.nickname = nickname;
            this.password = password;
        }

        public Member toEntity() {
            return Member.builder()
                    .username(username)
                    .nickname(nickname)
                    .password(password)
                    .build();
        }
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
    public static class Info{
        private String nickname;
        private String introduce;
        private String picture;
    }
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PutInfo{
        private String nickname;
        private String introduce;
    }
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Check{
        private String password;
    }

}
