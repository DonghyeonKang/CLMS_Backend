package com.example.clms.repository.user;

import com.example.clms.entity.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, String>, RefreshTokenRepository {
    Optional<RefreshToken> findByUser_Id(Long userId);
    Optional<RefreshToken> findByToken(String token);
    boolean existsByUser_Id(Long userId);
}
