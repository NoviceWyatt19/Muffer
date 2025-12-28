package io.github.wyatt.muffer.global.auth.paseto;

import dev.paseto.jpaseto.*;
import io.github.wyatt.muffer.domain.auth.service.CustomUserDetailsService;
import io.github.wyatt.muffer.domain.auth.token.RefreshToken;
import io.github.wyatt.muffer.domain.auth.token.RefreshTokenService;
import io.github.wyatt.muffer.global.auth.principal.CustomUserDetails;
import io.github.wyatt.muffer.global.auth.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasetoProvider {

    private final PasetoParser parser;
    private final SecretKey secretKey;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    /**
     * 토큰 생성
     * @param user: UserDetails 객체
     * @return token: roles 와 user id 포함
     */
    public String createAccessToken(CustomUserDetails user) {
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Pasetos.V2.LOCAL.builder()
                .setSharedSecret(this.secretKey) // 충돌 방지용 this, this 제거 무관
                .setSubject(user.getUsername()) // 유저 식별
                .setIssuedAt(Instant.now())
                .setExpiration(Instant.now().plus(3, ChronoUnit.MINUTES)) //NOTE 유효기간 3분 추후 변경 필요
                .claim("authorities", authorities)
                .claim("userId", user.member().getId())
                .claim("aid", UUID.randomUUID().toString())
                .compact();

    }

    public Authentication getAuthentication(String token) {
        Claims claims = parser.parse(token).getClaims();
        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        String rowAuthString = claims.get("authorities", String.class);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(rowAuthString.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserPrincipal principal = new UserPrincipal(userId, username, authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //TODO 검증 로직 삽입 필요
    public boolean validateToken(String token) {
        try {
            Paseto paseto = parser.parse(token);
            return true;
        }
        catch (PasetoSignatureException ex) {
            log.error("paseto signature is not valuable or use wrong key.");
        }
        catch (PasetoException e) {
            log.error("fail to validate token");
            //TODO 예외 처리나 토큰 Expiration 만료
        }
        return false;
    }

    public Claims parseClaims(String token) {
        return parser.parse(token).getClaims();
    }

    //TODO paseto exception 고려
    public boolean isAccessTokenExpired(String token) {
        Claims claims = parser.parse(token).getClaims();
        return claims.getExpiration().isBefore(Instant.now());
    }

    public String reissueAccessToken(Claims claims, String refreshTokenId) {
        String username = claims.getSubject();
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //TODO exception 재정의 필요 1
        RefreshToken refreshToken = refreshTokenService.findById(refreshTokenId).orElseThrow(
                () -> new RuntimeException("Not Found Refresh Token")
        );

        //TODO exception 재정의 필요 2
        if( !claims.get("aid", String.class).equals(refreshToken.aid()) ) {
            throw new RuntimeException("aid is miss matching");
        }

        return Pasetos.V2.LOCAL.builder()
                .setSharedSecret(this.secretKey) // 충돌 방지용 this, this 제거 무관
                .setSubject(userDetails.getUsername()) // 유저 식별
                .setIssuedAt(Instant.now())
                .setExpiration(Instant.now().plus(3, ChronoUnit.MINUTES)) //NOTE 유효기간 3분 추후 변경 필요
                .claim("authorities", authorities)
                .claim("userId", userDetails.member().getId())
                .claim("aid", refreshToken.aid())
                .compact();
    }

}
