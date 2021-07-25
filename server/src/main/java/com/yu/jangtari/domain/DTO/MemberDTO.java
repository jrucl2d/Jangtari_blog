package com.yu.jangtari.domain.DTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yu.jangtari.domain.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class MemberDTO {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Get {
        private String username;
        private String nickname;
        private String introduce;
        private String picture;

        @Builder
        public Get(String username, String nickname, String introduce, String picture) {
            this.username = username;
            this.nickname = nickname;
            this.introduce = introduce;
            this.picture = picture;
        }

        public static Get of(Member member) {
            return Get.builder()
                    .username(member.getUsername())
                    .nickname(member.getNickname())
                    .introduce(member.getIntroduce())
                    .picture(member.getPicture())
                    .build();
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
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update {
        @NotBlank(message = "닉네임이 빈칸이면 안 됩니다.")
        private String nickname;
        private String introduce;
        private MultipartFile picture;
        @JsonIgnore
        private String pictureURL;

        @Builder
        public Update(String nickname, String introduce, MultipartFile picture) {
            this.nickname = nickname;
            this.introduce = introduce;
            this.picture = picture;
        }

        @JsonIgnore
        public String getPictureURL() {
            return this.pictureURL;
        }

        public void setPictureURL(String pictureURL) {
            this.picture = null;
            this.pictureURL = pictureURL;
        }
    }
}
