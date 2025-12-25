package io.github.wyatt.muffer.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.wyatt.muffer.domain.member.auth.CustomUserDetailsService;
import io.github.wyatt.muffer.global.auth.filter.CustomLoginFilter;
import io.github.wyatt.muffer.global.auth.filter.PasetoAuthenticationFilter;
import io.github.wyatt.muffer.global.auth.paseto.PasetoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        // 매니저에게 유저를 찾는 방법과 비밀번호 비교 방법을 알려줌
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return builder.build();
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

        // H2 Console iframe 허용
        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );
        // 경로별 인가 설정 (추후 세부화)
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/join").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
        );

        // 필터 추가
        http
            .addFilterAt(new CustomLoginFilter(authenticationManager(http, passwordEncoder()), pasetoProvider, new ObjectMapper()),
                    UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new PasetoAuthenticationFilter(pasetoProvider),
                    CustomLoginFilter.class);
        return http.build();
    }

}
