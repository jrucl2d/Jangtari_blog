package com.yu.jangtari.domain;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@ToString
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of="id")
public class Member extends DateAuditing{
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
}
