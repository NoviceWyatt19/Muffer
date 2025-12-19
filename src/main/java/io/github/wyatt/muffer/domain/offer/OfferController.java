package io.github.wyatt.muffer.domain.offer;

import io.github.wyatt.muffer.domain.board.BoardService;
import io.github.wyatt.muffer.domain.offer.request.ChgStateReq;
import io.github.wyatt.muffer.domain.offer.request.OfrFilterReq;
import io.github.wyatt.muffer.global.config.ErrorCode;
import io.github.wyatt.muffer.global.exceptions.BusinessAccessDeniedException;
import io.github.wyatt.muffer.global.exceptions.BusinessNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final BoardService brdService;
    private final OfferService ofrService;

    @GetMapping("/{boardId}/list")
    public ResponseEntity<?> getOfferList(@PathVariable Long boardId,@RequestBody OfrFilterReq req) {
        log.info("REQUEST - get offer list : boardId -> {}, req -> {}", boardId, req);
        return ResponseEntity.ok().body(ofrService.getOfferList(boardId, req));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOffer(@RequestBody Offer offer) {
        log.info("REQUEST - create offer : offer -> {}", offer);
        ofrService.saveOffer(offer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("{boardId}/update-state")
    public ResponseEntity<?> changeState(
        @PathVariable("boardId") Long boardId,
        @RequestBody ChgStateReq req
    ) {
        Long requesterId = 1L;
        ofrService.changeState(boardId, requesterId, req);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(BusinessNotFoundException.class)
    private ResponseEntity<?> notFoundExceptionHandler() {
        log.error("not found data.");
        return ResponseEntity.status(ErrorCode.NOT_FOUND_DATA.getHttpStatus())
                .build();
    }

    @ExceptionHandler(BusinessAccessDeniedException.class)
    private ResponseEntity<?> accessDeniedExceptionHandler() {
        log.error("request can not access.");
        return ResponseEntity.status(ErrorCode.OTHER_USER.getHttpStatus()).build();
    }


}
