package com.yu.jangtari.api.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * jwtHash는 access jwt token을 hashing 한 결과로, 일종의 refresh token의 역할을 함
 */
@Entity
@Getter
@Table(name = "jwt_token")
@EqualsAndHashCode(of = {"username", "jwtHash"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtToken {
    @Id
    String username;
    @Column(nullable = false)
    String jwtHash;

    @Builder
    private JwtToken(String username, String jwtHash) {
        this.username = username;
        this.jwtHash = jwtHash;
    }
}
