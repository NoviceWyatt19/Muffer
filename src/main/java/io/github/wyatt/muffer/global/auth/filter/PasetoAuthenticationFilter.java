package io.github.wyatt.muffer.global.auth.filter;

import dev.paseto.jpaseto.Claims;
import io.github.wyatt.muffer.domain.auth.token.TokenType;
import io.github.wyatt.muffer.global.auth.paseto.PasetoProvider;
import io.github.wyatt.muffer.global.config.ErrorCode;
import io.github.wyatt.muffer.global.exceptions.UnauthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class PasetoAuthenticationFilter extends OncePerRequestFilter {

    private final PasetoProvider pasetoProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/") || path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/h2-console");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        // 토큰이 없거나 Bearer prefix 가 없는 경우
        if (authorization == null || authorization.isBlank() || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.substring(7);
        String refreshTokenId = resolveRefreshToken(request);

        if (!pasetoProvider.validateToken(accessToken)) {
            throw new UnauthenticationException(ErrorCode.AUTHENTICATION_FAILED);
        }

        // access token is not expired
        if (!pasetoProvider.isAccessTokenExpired(accessToken)) {
            Authentication authentication = pasetoProvider.getAuthentication(accessToken);
            // SecurityContext 에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = pasetoProvider.parseClaims(accessToken);

        // is not empty refresh token
        if (refreshTokenId == null) {
            throw new UnauthenticationException(ErrorCode.AUTHENTICATION_FAILED);
        }

        String reissuedAccessToken = pasetoProvider.reissueAccessToken(claims, refreshTokenId);
        //NOTE 프론트에서도 authentication overwrite 의도 확인 필요
        response.setHeader("Authorization", "Bearer " + reissuedAccessToken);
        Authentication authentication = pasetoProvider.getAuthentication(reissuedAccessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(c -> TokenType.REFRESH_TOKEN.name().equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

}
