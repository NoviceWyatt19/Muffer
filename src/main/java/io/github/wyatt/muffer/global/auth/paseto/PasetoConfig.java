package io.github.wyatt.muffer.global.auth.paseto;

import dev.paseto.jpaseto.PasetoParser;
import dev.paseto.jpaseto.Pasetos;
import dev.paseto.jpaseto.lang.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class PasetoConfig {

    @Value("${paseto.secret}")
    private String secretString;

    @Bean
    public SecretKey secretKey() {
        return Keys.secretKey(secretString.getBytes());
    }

    @Bean
    public PasetoParser pasetoParser(SecretKey secretKey) {
        return Pasetos.parserBuilder()
                .setSharedSecret(secretKey)
                .build();
    }
}
