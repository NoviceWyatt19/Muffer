package io.github.wyatt.muffer.domain.board.repository;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepo extends JpaRepository<Board, Integer> {

    @Modifying
    @Query("update Board b set b.status = :state where b.id = :boardId")
    void updateState(@Param("boardId") int boardId,@Param("state") BoardStatus state);

    @Query("select b from Board b where b.status != 'HIDDEN'")
    List<Board> findAllWithoutHidden();
}
