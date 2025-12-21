package io.github.wyatt.muffer.global.config;

import io.github.wyatt.muffer.global.auth.filter.CustomLoginFilter;
import io.github.wyatt.muffer.global.auth.filter.PasetoAuthenticationFilter;
import io.github.wyatt.muffer.global.auth.paseto.PasetoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasetoProvider pasetoProvider;
    private final AuthenticationConfiguration authConfig;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF, Form Login, HTTP Basic 비활성화 (Stateless 구조) + CORS 비활성화(개발 편의, 추후 제거 예정)
        http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable);

        // 세션 정책 설정: stateless
        http.sessionManagement(
                session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        // 경로별 인가 설정 (추후 세부화)
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/join").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
        );

        // 필터 추가
        http
            .addFilterAt(new CustomLoginFilter(authenticationManager(authConfig), pasetoProvider),
                    UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new PasetoAuthenticationFilter(pasetoProvider),
                    CustomLoginFilter.class);
        return http.build();
    }

}
