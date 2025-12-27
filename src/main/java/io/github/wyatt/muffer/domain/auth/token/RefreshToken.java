package io.github.wyatt.muffer.domain.auth.token;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "refresh_token")
public record RefreshToken(
        @Id String token,
        String aid, // access token 의 claims 에 저장된 uuid 값
        @TimeToLive long ttl
) {
    public RefreshToken update(long ttl) {
        return new RefreshToken(token, this.aid, ttl);
    }

    public RefreshToken expiration(String token) {
        return new RefreshToken(this.token, this.aid, 0);
    }

}
