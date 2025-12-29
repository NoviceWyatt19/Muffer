package io.github.wyatt.muffer.global.auth.filter;

import dev.paseto.jpaseto.Claims;
import dev.paseto.jpaseto.PasetoException;
import io.github.wyatt.muffer.domain.auth.service.AuthService;
import io.github.wyatt.muffer.domain.auth.token.RefreshToken;
import io.github.wyatt.muffer.domain.auth.token.ReissueTokens;
import io.github.wyatt.muffer.domain.auth.token.TokenType;
import io.github.wyatt.muffer.global.auth.paseto.PasetoProvider;
import io.github.wyatt.muffer.global.auth.utils.TokenCookieFactory;
import io.github.wyatt.muffer.global.config.ErrorCode;
import io.github.wyatt.muffer.global.exceptions.UnauthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class PasetoAuthenticationFilter extends OncePerRequestFilter {

    private final PasetoProvider pasetoProvider;
    private final AuthService authService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/") || path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/h2-console");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        // 토큰 없음
        if (authorization == null || authorization.isBlank() || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.substring(7);

        try {
            // 정상 토큰 인증
            Authentication authentication = pasetoProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 인증 성공
            filterChain.doFilter(request, response);

        } catch (PasetoException ex) {
            if (isExpiredException(ex)) {
                log.info("Access token expired, start reissue");
                // 만료 시 재발급
                handleExpiredToken(request, response, filterChain, accessToken);
            } else {
                // 기타 에러
                log.error("Invalid Paseto Token", ex);
                throw new UnauthenticationException(ErrorCode.AUTHENTICATION_FAILED);
            }
        }
    }

    private boolean isExpiredException(PasetoException ex) {
        return ex.getMessage() != null && ex.getMessage().contains("expired");
    }

    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String accessToken) {
        String refreshTokenId = resolveRefreshToken(request);
        if (refreshTokenId == null) {
            throw new UnauthenticationException(ErrorCode.AUTHENTICATION_FAILED);
        }
        try {
            ReissueTokens newTokens = authService.reissueAccessToken(refreshTokenId);

            // access token
            response.setHeader("Authorization", "Bearer " + newTokens.accessToken());

            // refresh token
            RefreshToken newRefreshToken = newTokens.refreshToken();
            response.addHeader("Set-Cookie", TokenCookieFactory.createTokenCookie(
                    TokenType.REFRESH_TOKEN.name(), newRefreshToken.token(), newRefreshToken.ttl()).toString()
            );

            Authentication authentication = pasetoProvider.getAuthentication(newTokens.accessToken());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Reissue Failed", e);
            throw new UnauthenticationException(ErrorCode.AUTHENTICATION_FAILED);
        }

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
