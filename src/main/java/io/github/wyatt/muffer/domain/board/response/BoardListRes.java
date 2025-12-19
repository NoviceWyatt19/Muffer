package io.github.wyatt.muffer.domain.board.response;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.domain.board.entity.Board;

import java.time.LocalDateTime;

public record BoardListRes(
        String title,
        Long sellerId,
        Long productId,
        String sellerName,
        BoardStatus status,
        String productBrand,
        String productName,
        int initialPrice,
        LocalDateTime createdAt
) {
    public static BoardListRes from(Board board, String brandName) {
        return new BoardListRes(
                board.getTitle(),
                board.getMemberId(),
                board.getProduct().getId(),
                board.getProduct().getName(),
                board.getStatus(),
                brandName,
                board.getProduct().getName(),
                board.getInitialPrice(),
                board.getCreatedAt()
        );
    }
}
