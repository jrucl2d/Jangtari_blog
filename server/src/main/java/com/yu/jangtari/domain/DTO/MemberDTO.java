package com.yu.jangtari.domain.DTO;


import com.yu.jangtari.domain.Member;
import lombok.*;

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
