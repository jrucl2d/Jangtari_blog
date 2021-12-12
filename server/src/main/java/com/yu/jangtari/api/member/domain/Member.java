package com.yu.jangtari.api.member.domain;

import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends DateAuditing
{
    @Id
    @Column(name = "username")
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private String introduce;

    private String picture;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Builder
    public Member(String username, String nickname, String password, RoleType roleType, String introduce, String picture) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.introduce = introduce;
        this.picture = picture;
        this.role = roleType == null ? RoleType.USER : RoleType.ADMIN;
        this.deleteFlag = new DeleteFlag();
    }

    public Member updateMember(MemberDto.Update memberDto) {
        return Member.builder()
            .username(this.username)
            .nickname(memberDto.getNickname())
            .password(this.password)
            .introduce(memberDto.getIntroduce())
            .roleType(RoleType.ADMIN)
            .build();
    }

    public void delete() {
        this.deleteFlag.softDelete();
    }

}
