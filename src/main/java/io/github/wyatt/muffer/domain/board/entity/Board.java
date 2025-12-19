package io.github.wyatt.muffer.domain.board.entity;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.global.config.ErrorCode;
import io.github.wyatt.muffer.global.entity.BaseEntity;
import io.github.wyatt.muffer.global.exceptions.ForbiddenModifyException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Long memberId;

    private String title;

    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    private int initialPrice;
    private int exposureLevel;

    @Builder
    private Board(Product product, String title, BoardStatus status, int initialPrice, int exposureLevel,  Long memberId) {
        this.title = title;
        this.status = status;
        this.product = product;
        this.initialPrice = initialPrice;
        this.exposureLevel = exposureLevel;
        this.memberId = memberId;
    }

    public static Board create(String title, BoardStatus status, Product product, int initialPrice, int exposureLevel, Long memberId){
        return Board.builder()
                .title(title)
                .status(status)
                .product(product)
                .initialPrice(initialPrice)
                .exposureLevel(exposureLevel)
                .memberId(memberId)
                .build();
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", initialPrice=" + initialPrice +
                ", exposureLevel=" + exposureLevel +
                ", product=" + product.toString() +
                '}';
    }

    public void changeStatus(BoardStatus state) throws ForbiddenModifyException {
        if(this.status == BoardStatus.SOLD_OUT || this.status == BoardStatus.DEAL_AGREED) {
            throw new ForbiddenModifyException(ErrorCode.FORBIDDEN_MODIFY);
        }
        this.status = state;
    }
}
