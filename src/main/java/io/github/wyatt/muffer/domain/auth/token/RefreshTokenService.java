package io.github.wyatt.muffer.domain.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRedisRepo refreshTokenRepo;
    private final ObjectMapper objectMapper;

    @Value("${paseto.refresh-token.expiration}")
    private Long exp;

    public RefreshToken create(String aid, String username) {
        RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), username, aid, exp);
        log.info("save refresh token -> {}", refreshToken);
        refreshTokenRepo.save(refreshToken);
        return refreshToken;
    }

    public void create(RefreshToken refreshToken) {
        refreshTokenRepo.save(refreshToken);
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepo.delete(refreshToken);
    }

    public void deleteToken(String refreshTokenId) {
        refreshTokenRepo.findById(refreshTokenId).ifPresent(token -> {
            refreshTokenRepo.delete(token);
            log.info("Successfully deleted refresh token from Redis: {}", refreshTokenId);
        });
    }

    public boolean isRefreshTokenExisted(String aid) {
        return refreshTokenRepo.existsByAid(aid);
    }

    public Optional<RefreshToken> findByAid(String aid) {
        return refreshTokenRepo.findByAid(aid);
    }

    public Optional<RefreshToken> findById(String refreshTokenId) {
        return refreshTokenRepo.findById(refreshTokenId);
    }
}
