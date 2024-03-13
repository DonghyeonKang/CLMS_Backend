package com.example.clms.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.clms.common.CookieUtil;
import com.example.clms.common.exception.ErrorCode;
import com.example.clms.common.exception.RefreshTokenNotFoundException;
import com.example.clms.entity.auth.RefreshToken;
import com.example.clms.entity.user.User;
import com.example.clms.repository.user.RefreshTokenRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
@Transactional // TODO: transaction 리팩토링
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final EntityManager entityManager;
    // token 으로 사용자 조회
    public User getUserByRefreshToken(String token) {
        System.out.println(token);
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        return refreshToken.getUser();
    }

    // access token 생성(로그인 시 사용)
    public String createAccessToken(Long id, String username) {
        return JWT.create()
                .withSubject(JwtProperties.ACCESS_TOKEN)  // token 이름
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME)) // 만료시간
                .withClaim("userId", String.valueOf(id))    // token payload 에서 private 추가
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET)); // 해싱 알고리즘 선택, 시크릿 키
    }

    // refresh token 생성(로그인 시 사용)
    public String createRefreshToken() {
        return JWT.create()
                .withSubject(JwtProperties.REFRESH_TOKEN) // token 이름
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME)) // 만료시간
                .sign(Algorithm.HMAC512(JwtProperties.SECRET)); // 해싱 알고리즘 선택 시크릿 키
    }

    // refresh token db 에 저장(로그인 시 사용, 갱신 시 사용)
    public void setRefreshToken(String token, User user) {
        if(refreshTokenRepository.existsByUser_Id(user.getId())) { // 이미 token 이 존재하면 업데이트
            updateRefreshToken(token, user);
        } else {    // token 이 존재하지 않으면 생성
            saveRefreshToken(token, user);
        }
    }

    // refresh token 갱신
    @Transactional
     private void updateRefreshToken(String token, User user) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        // 엔티티 영속성 설정
        entityManager.persist(refreshToken);

        // dirty check 로 업데이트
        refreshToken.updateToken(token);
    }

    // refresh token 저장
    @Transactional
    private void saveRefreshToken(String token, User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    // refresh token 삭제
    @Transactional
    public void removeRefreshToken(String token) {
        refreshTokenRepository.delete(refreshTokenRepository.findByToken(token) // TODO: delete by token 으로 최적화
                .orElseThrow());
    }

    // 로그아웃
    public void logout(HttpServletRequest req) {
        CookieUtil cookieUtil = new CookieUtil(req);
        removeRefreshToken(cookieUtil.getValue("refresh_token"));
    }

    // 토큰 유효성 체크(복호화)
    public DecodedJWT checkTokenValid(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
        } catch (TokenExpiredException | InvalidClaimException | SignatureVerificationException ee) {
            return null;
        }
    }

    // refresh token 업데이트 체크 TODO: isNeedToUpdate 이용해서 새로운 로직으로 구현
    public boolean isNeedToUpdate(String token) {
        try {
            Date expiresAt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                    .build()
                    .verify(token)
                    .getExpiresAt();

            Date current = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, 7);

            Date after7dayFromToday = calendar.getTime();

            // 7일 이내 만료
            if(expiresAt.before(after7dayFromToday)) {
                log.info("리프레시 토큰이 7일 이내 만료됩니다. ");
                return true;
            }
        } catch (TokenExpiredException e) {
            return true;
        }
        return false;
    }
}
