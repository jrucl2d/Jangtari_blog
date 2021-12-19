package com.yu.jangtari.api.member.dto;

import com.yu.jangtari.api.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto
{
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LogInForm {
        private String username;
        private String password;

        public LogInForm(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Get {
        private String username;
        private String nickname;
        private String introduce;
        private String picture;

        @Builder
        private Get(String username, String nickname, String introduce, String picture) {
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

        public Member toEntity(PasswordEncoder passwordEncoder) {
            return Member.builder()
                    .username(this.username)
                    .nickname(this.nickname)
                    .password(passwordEncoder.encode(this.password))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update {
        @NotNull(message = "수정할 회원 이름이 비어있으면 안 됩니다.")
        private String username;

        @NotBlank(message = "닉네임이 빈칸이면 안 됩니다.")
        private String nickname;

        private String introduce;

        @Builder
        public Update(String username, String nickname, String introduce) {
            this.username = username;
            this.nickname = nickname;
            this.introduce = introduce;
        }
    }
}
