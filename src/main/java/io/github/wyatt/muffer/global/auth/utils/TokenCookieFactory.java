package io.github.wyatt.muffer.global.auth.utils;

import org.springframework.http.ResponseCookie;

public class TokenCookieFactory {
    public static ResponseCookie createTokenCookie(String name, String value, Long expiration) {
        return ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true) // xss 방지
                .secure(false) // https 강제
                .sameSite("None") // cors 방지
                .maxAge(expiration)
                .build();
    }
    public static ResponseCookie createExpiredToken(String name) {
        return ResponseCookie.from(name, "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("None")
                .maxAge(0)
                .build();
    }
}
