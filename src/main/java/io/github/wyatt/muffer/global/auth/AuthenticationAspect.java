package io.github.wyatt.muffer.global.auth;

import dev.paseto.jpaseto.PasetoParser;
import io.github.wyatt.muffer.global.auth.paseto.PasetoConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.sasl.AuthenticationException;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final PasetoParser parser;

    @Around("@annotation(io.github.wyatt.muffer.global.auth.AuthRequired)")
    public Object authenticate(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest req = ( (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String authHeader = req.getHeader("Authorization");
        if (authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Invalid authorization header");
        }

        String token = authHeader.split(" ")[1]; // "Bearer "제거
        try {
            String subject = parser.parse(token).getClaims().getSubject();
            // 인증 토큰 발행
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(subject, null, List.of()));
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid token");
        }

        return joinPoint.proceed();
    }
}
