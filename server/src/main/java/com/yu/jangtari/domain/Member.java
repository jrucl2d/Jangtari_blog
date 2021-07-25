package com.yu.jangtari.domain;
import com.yu.jangtari.domain.DTO.MemberDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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

@Getter
@ToString
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of={"id", "username"}, callSuper = false) // 상속받은 객체에서 발생하는 에러 문구 제거
public class Member extends DateAuditing {
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
    public Member(String username, String nickname, String password, String introduce, String picture) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.introduce = introduce;
        this.picture = picture;
        this.role = RoleType.USER;
        this.deleteFlag = DeleteFlag.initDeleteFlag();
    }

    public void updateMember(MemberDTO.Update memberDTO) {
        this.nickname = memberDTO.getNickname();
        this.introduce = memberDTO.getIntroduce();
        final String pictureURL = memberDTO.getPictureURL();
        if (pictureURL != null) this.picture = pictureURL;
    }
}
