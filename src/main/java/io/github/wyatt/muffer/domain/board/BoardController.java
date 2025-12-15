package io.github.wyatt.muffer.domain.board;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.domain.board.request.BoardFilterReq;
import io.github.wyatt.muffer.domain.board.request.BoardSvReq;
import io.github.wyatt.muffer.global.exceptions.BusinessAccessDeniedException;
import io.github.wyatt.muffer.global.exceptions.ForbiddenModifyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@Slf4j
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
        //NOTE 추후 PASETO 붙이면 거기서 memberId 가져오기
        int memberId = 1;
        boardService.updateState(memberId, boardId, state);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ForbiddenModifyException.class)
    private ResponseEntity<?> forbiddenModifyExceptionHandler() {
        log.error("forbidden modify.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(BusinessAccessDeniedException.class)
    private ResponseEntity<?> businessAccessDeniedExceptionHandler() {
        log.error("requester can not access.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
