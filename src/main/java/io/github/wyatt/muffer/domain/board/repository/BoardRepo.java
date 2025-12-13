package io.github.wyatt.muffer.domain.board.repository;

import io.github.wyatt.muffer.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepo extends JpaRepository<Board, Integer> {

}
