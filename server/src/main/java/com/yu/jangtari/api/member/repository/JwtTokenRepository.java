package com.yu.jangtari.api.member.repository;

import com.yu.jangtari.api.member.domain.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepository extends JpaRepository<JwtToken, String> {
}
