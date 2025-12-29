package io.github.wyatt.muffer.domain.auth.token;

public record ReissueTokens(
        String accessToken,
        RefreshToken refreshToken
) {
}
