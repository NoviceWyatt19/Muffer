package io.github.wyatt.muffer.domain.auth.token;


import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepo extends CrudRepository<RefreshToken, String> {

}
