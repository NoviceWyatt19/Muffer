package io.github.wyatt.muffer.global.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

//    @Value("${spring.data.redis.username}")
//    private String username;
//
//    @Value("${spring.data.redis.password}")
//    private String password;

    private ApplicationContext applicationContext;

    /**
     * Redis server 와 연결 설정
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setPort(port);
        configuration.setHostName(host);
//        configuration.setUsername(username);
//        configuration.setPassword(password);
        return new LettuceConnectionFactory(configuration);
    }

    /**
     * Redis w/r 설정 (직렬화 정의)
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    /**
     * session data 를 JSON 변환해 저장하여 DTO 를 그대로 저장할 수 있도록 설정
     */
//    @Bean NOTE 현재 수준에서 불필요.
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
//
//        ClassLoader loader = getClass().getClassLoader();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModules( SecurityJackson2Modules.getModules(loader));
//
//        // Java 8 Date/Time API 지원을 위한 모듈 등록 (필수 권장)
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 날짜를 타임스탬프가 아닌 문자열로 직렬화
//
//        mapper.activateDefaultTyping(
//                BasicPolymorphicTypeValidator.builder()
//                        .allowIfBaseType("org.springframework.security.")
//                        .allowIfBaseType("io.github.wyatt.")
//                        .build(),
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//        return new GenericJackson2JsonRedisSerializer(mapper);
//    }

}