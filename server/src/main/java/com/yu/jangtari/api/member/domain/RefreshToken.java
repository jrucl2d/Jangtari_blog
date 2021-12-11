package com.yu.jangtari.api.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * jwtHash는 access jwt token을 hashing 한 결과로, 일종의 refresh token의 역할을 함
 */
@Entity
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken
{
    @Id
    String username;
    @Column(nullable = false)
    String refreshToken;

    @Builder
    private RefreshToken(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }
}
