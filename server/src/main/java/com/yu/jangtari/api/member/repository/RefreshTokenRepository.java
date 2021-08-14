package com.yu.jangtari.api.member.repository;

import com.yu.jangtari.api.member.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
