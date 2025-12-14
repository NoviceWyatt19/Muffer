package io.github.wyatt.muffer.domain.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OptionRepo extends JpaRepository<Option, Integer> {

    @Query("select o.name from Option o where o.id = :brandId")
    Optional<String>  findNameById(@Param("brandId") int brandId);
}
