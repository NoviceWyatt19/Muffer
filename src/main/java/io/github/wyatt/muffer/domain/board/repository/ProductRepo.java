package io.github.wyatt.muffer.domain.board.repository;

import io.github.wyatt.muffer.domain.board.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {
//    Optional<Product> findById(int id);
}
