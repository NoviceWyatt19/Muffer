package io.github.wyatt.muffer.domain.board.response;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.domain.board.entity.Board;

import java.time.LocalDateTime;

public record BoardListRes(
        String title,
        int sellerId,
        String sellerName,
        BoardStatus status,
        String productBrand,
        int initialPrice,
        LocalDateTime createdAt
) {
    public static BoardListRes from(Board board) {
        return new BoardListRes(
                board.getTitle(),
                board.getProduct().getId(),
                board.getProduct().getName(),
                board.getStatus(),
                null,
                board.getInitialPrice(),
                board.getCreatedAt()
        );
    }
}
