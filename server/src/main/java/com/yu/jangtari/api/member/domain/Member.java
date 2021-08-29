package com.yu.jangtari.api.member.domain;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.common.DateAuditing;
import com.yu.jangtari.common.DeleteFlag;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@ToString
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends DateAuditing
{
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    private String introduce;

    private String picture;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Embedded
    DeleteFlag deleteFlag;

    @Builder
    public Member(Long id, String username, String nickname, String password, RoleType roleType, String introduce, String picture) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.introduce = introduce;
        this.picture = picture;
        this.role = roleType == null ? RoleType.USER : RoleType.ADMIN;
        this.deleteFlag = new DeleteFlag();
    }

    public Member updateMember(MemberDto.Update memberDto) {
        String pictureUrl = memberDto.getPictureUrl();
        if (pictureUrl != null) this.picture = pictureUrl;

        return Member.builder()
            .id(this.id)
            .username(this.username)
            .nickname(memberDto.getNickname())
            .password(this.password)
            .introduce(memberDto.getIntroduce())
            .picture(pictureUrl)
            .roleType(RoleType.ADMIN)
            .build();
    }

    public void delete() {
        if (this.id == 1L) throw new BusinessException(ErrorCode.ADMIN_DELETE_ERROR);
        this.deleteFlag.softDelete();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id)
            && username.equals(member.username)
            && Objects.equals(nickname, member.nickname)
            && Objects.equals(password, member.password)
            && Objects.equals(introduce, member.introduce)
            && Objects.equals(picture, member.picture)
            && role == member.role
            && Objects.equals(deleteFlag, member.deleteFlag);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, username, nickname, password, introduce, picture, role, deleteFlag);
    }
}
