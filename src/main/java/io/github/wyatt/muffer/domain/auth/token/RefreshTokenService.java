package io.github.wyatt.muffer.domain.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

//TODO Redis 나 Refresh 관련 위치 재정리
@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRedisRepo refreshTokenRepo;
    private final ObjectMapper objectMapper;

    @Value("${paseto.expiration}")
    private Long exp;

    public RefreshToken save(String aid) {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), aid, exp);
        log.info("save refresh token -> {}", refreshToken);
        refreshTokenRepo.save(refreshToken);
        return refreshToken;
    }

    public void deleteToken(String refreshToken) {
        refreshTokenRepo.deleteById(refreshToken);
        log.info("delete refresh token -> {}", refreshToken);
    }
}
