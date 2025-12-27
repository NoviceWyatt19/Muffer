package io.github.wyatt.muffer.global.auth.utils;

import org.springframework.http.ResponseCookie;

//NOTE 로컬 개발 설정: secure(false), sameSite("Lax") / 배포 환경 설정: secure(true), sameSite("None")
public class TokenCookieFactory {
    public static ResponseCookie createTokenCookie(String name, String value, Long expiration) {
        return ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true) // js 접근 차단 여부
                .secure(false) // https 강제 여부
                .sameSite("Lax") // csrf
                .maxAge(expiration)
                .build();
    }
    public static ResponseCookie createExpiredToken(String name) {
        return ResponseCookie.from(name, "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(0)
                .build();
    }
}
