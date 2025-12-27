package io.github.wyatt.muffer.global.auth.principal;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserPrincipal(
        Long userId,
        String email,
        Collection<? extends GrantedAuthority> authorities
) {
}