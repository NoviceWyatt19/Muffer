package io.github.wyatt.muffer.domain.auth.token;


import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepo extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByAid(String aid);

    boolean existsByAid(String aid);

    void deleteByAid(String aid);
}
