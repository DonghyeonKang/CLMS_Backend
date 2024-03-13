package com.example.clms.repository.user;

import com.example.clms.entity.auth.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByUser_Id(Long userId);
    Optional<RefreshToken> findByToken(String token);
    boolean existsByUser_Id(Long userId);
    RefreshToken save(RefreshToken refreshToken);
    void delete(RefreshToken refreshToken);

}
