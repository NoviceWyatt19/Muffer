package io.github.wyatt.muffer.global.auth.filter;

public record LoginRequest(
    String username, // email
    String password
) {
}
