package io.github.wyatt.muffer.global.auth.paseto;

import dev.paseto.jpaseto.Claims;
import dev.paseto.jpaseto.PasetoParser;
import dev.paseto.jpaseto.Pasetos;
import io.github.wyatt.muffer.domain.member.auth.CustomUserDetails;
import io.github.wyatt.muffer.domain.member.auth.CustomUserDetailsService;
import io.github.wyatt.muffer.domain.member.auth.UserPrincipal;
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
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasetoProvider {

    private final PasetoParser parser;
    private final SecretKey secretKey;
    private final CustomUserDetailsService userDetailsService;

    /**
     * 토큰 생성
     * @param user: UserDetails 객체
     * @return token: roles 와 user id 포함
     */
    public String createToken(CustomUserDetails user) {
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

    public boolean validateToken(String token) {
        try {
            parser.parse(token);
            return true;
        } catch (Exception e) {
            log.error("fail to validate token");
            //TODO 예외 처리나 토큰 Expiration 만료
            return false;
        }
    }

}
