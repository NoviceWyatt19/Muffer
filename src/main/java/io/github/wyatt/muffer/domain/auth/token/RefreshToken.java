package io.github.wyatt.muffer.domain.auth.token;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refresh_token")
public record RefreshToken(
        @Id
        String token,
        String username,
        @Indexed
        String aid, // access token 의 claims 에 저장된 uuid 값
        @TimeToLive
        long ttl
) {
    public RefreshToken update(long ttl) {
        return new RefreshToken(token, this.aid, this.username, ttl);
    }

    public RefreshToken expiration() {
        return new RefreshToken(this.token, this.aid, this.username, 0);
    }

}
