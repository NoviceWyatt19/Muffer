package io.github.wyatt.muffer.domain.board;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.domain.board.request.BoardFilterReq;
import io.github.wyatt.muffer.domain.board.request.BoardSvReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/boards")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<?> getBoardList() {
        return ResponseEntity.ok().body(boardService.getBoards(new BoardFilterReq()));
    }

    @PostMapping("/{memberId}/create")
    public ResponseEntity<?> createBoard(@PathVariable("memberId") int memberId, @RequestBody BoardSvReq req) {
        boardService.saveBoard(req, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "success"));
    }

    @PatchMapping("{boardId}/state")
    public ResponseEntity<?> updateState(
            @PathVariable int boardId,
            @RequestParam BoardStatus state
    ) {
        boardService.updateState(boardId, state);
        return ResponseEntity.ok().build();
    }
}
