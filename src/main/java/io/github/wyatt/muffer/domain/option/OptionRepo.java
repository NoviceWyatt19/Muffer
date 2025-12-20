package io.github.wyatt.muffer.domain.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionRepo extends JpaRepository<Option, Long> {

    @Query("select o.name from Option o where o.id = :brandId")
    Optional<String>  findNameById(@Param("brandId") Long brandId);
}
