package io.github.wyatt.muffer.domain.board.repository;

import io.github.wyatt.muffer.domain.board.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
}
