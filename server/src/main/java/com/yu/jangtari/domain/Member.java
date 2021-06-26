package com.yu.jangtari.domain;
import com.yu.jangtari.domain.DTO.MemberDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

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

    public void updateNickNameAndIntoduce(MemberDTO.Update memberDTO) {
        this.nickname = memberDTO.getNickname();
        this.introduce = memberDTO.getIntroduce();
    }

    public void initPicture(String pictureURL) {
        this.picture = pictureURL;
    }
}
