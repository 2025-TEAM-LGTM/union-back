package com.union.demo.repository;

import com.union.demo.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

    long deleteByExpiresAtBefore(Instant now);

    long deleteByUser_UserId(Long userId);

    boolean existsByToken(String token);

    void deleteByToken(String token);
}
