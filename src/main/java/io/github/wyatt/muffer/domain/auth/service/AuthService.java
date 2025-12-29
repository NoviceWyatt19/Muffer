package io.github.wyatt.muffer.domain.auth.service;

import dev.paseto.jpaseto.Claims;
import io.github.wyatt.muffer.domain.auth.request.SignUpReq;
import io.github.wyatt.muffer.domain.auth.token.RefreshToken;
import io.github.wyatt.muffer.domain.auth.token.RefreshTokenService;
import io.github.wyatt.muffer.domain.auth.token.ReissueTokens;
import io.github.wyatt.muffer.domain.member.Member;
import io.github.wyatt.muffer.domain.member.MemberRepo;
import io.github.wyatt.muffer.domain.member.Role;
import io.github.wyatt.muffer.global.auth.paseto.PasetoProvider;
import io.github.wyatt.muffer.global.auth.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepo mRepo;
    private final PasswordEncoder pwdEncoder;
    private final RefreshTokenService refreshTokenService;
    private final PasetoProvider pasetoProvider;

    public void signUp(SignUpReq req) {
        String encodedPassword = pwdEncoder.encode(req.password());
        log.info("create member -> {}", req);
        Member member = req.toEntity(encodedPassword, Role.USER);
        mRepo.save(member);
    }

    public void logout(String refreshToken) {
        long start = System.currentTimeMillis();
        refreshTokenService.deleteToken(refreshToken);
        log.info("delete refresh token -> {}", refreshToken);
        log.debug(">>> [Time] 로그아웃(Redis 삭제) 소요시간: {} ms", System.currentTimeMillis() - start);
    }

    @Transactional
    public ReissueTokens reissueAccessToken(String refreshTokenId) {

        // Redis 검증
        RefreshToken refreshToken = refreshTokenService.findById(refreshTokenId)
                .orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));

        // 유저 정보 조회
        Member member = mRepo.findByEmail(refreshToken.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("access token reissue - auth service");

        // refresh token 삭제
        refreshTokenService.delete(refreshToken);

        // 토큰 재발급
        String newAccessToken = pasetoProvider.createAccessToken(new CustomUserDetails(member));
        Claims claims = pasetoProvider.parseClaims(newAccessToken); //TODO 파싱 비용 고려
        RefreshToken newRefreshToken = refreshTokenService.create(claims.get("aid", String.class), claims.getSubject());

        return new ReissueTokens(newAccessToken, newRefreshToken);
    }
}
