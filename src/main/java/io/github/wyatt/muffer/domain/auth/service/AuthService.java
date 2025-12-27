package io.github.wyatt.muffer.domain.auth.service;

import io.github.wyatt.muffer.domain.auth.request.SignUpReq;
import io.github.wyatt.muffer.domain.member.Member;
import io.github.wyatt.muffer.domain.member.MemberRepo;
import io.github.wyatt.muffer.domain.member.Role;
import io.github.wyatt.muffer.domain.auth.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepo mRepo;
    private final PasswordEncoder pwdEncoder;
    private final RefreshTokenService refreshTokenService;

    public void signUp(SignUpReq req) {
        String encodedPassword = pwdEncoder.encode(req.password());
        log.info("create member -> {}", req);
        Member member = req.toEntity(encodedPassword, Role.USER);
        mRepo.save(member);
    }

    public void logout(String refreshToken) {
        refreshTokenService.deleteToken(refreshToken);
        log.info("delete refresh token -> {}", refreshToken);
    }

/*    public RefreshToken signIn(SignInReq req) {
        Member member = mRepo.findByEmail(req.email()).get();
        String accessToken = pasetoProvider.createAccessToken(new CustomUserDetails(member));
        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                pasetoProvider.parseClaims(accessToken).get("aid", String.class),
                refreshExp
        );
        return refreshToken;
    }*/
}
