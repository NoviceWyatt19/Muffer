package io.github.wyatt.muffer.domain.board;

import io.github.wyatt.muffer.domain.board.request.BoardFilterReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("Find All Boards")
    void findAllTest() {
        BoardFilterReq req = new BoardFilterReq();
        boardService.getBoards(req);
    }

}