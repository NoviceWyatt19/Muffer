package io.github.wyatt.muffer.domain.board.entity;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.global.entity.BaseEntity;
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
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String title;

    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    private int brandOptionId;
    private String customBrand;

    private int initialPrice;
    private int exposureLevel;

    @Builder
    private Board(Product product, String title, BoardStatus status, int brandOptionId, String customBrand, int initialPrice, int exposureLevel) {
        this.title = title;
        this.status = status;
        this.product = product;
        this.brandOptionId = brandOptionId;
        this.customBrand = customBrand;
        this.initialPrice = initialPrice;
        this.exposureLevel = exposureLevel;
    }

    public static Board create(String title, BoardStatus status, Product product, int brandOptionId, String customBrand, int initialPrice, int exposureLevel){
        return Board.builder()
                .title(title)
                .status(status)
                .product(product)
                .brandOptionId(brandOptionId)
                .customBrand(customBrand)
                .initialPrice(initialPrice)
                .exposureLevel(exposureLevel)
                .build();
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", brandOptionId=" + brandOptionId +
                ", customBrand='" + customBrand + '\'' +
                ", initialPrice=" + initialPrice +
                ", exposureLevel=" + exposureLevel +
                ", product=" + product.toString() +
                '}';
    }
}
