package io.github.wyatt.muffer.domain.board.request;

import io.github.wyatt.muffer.domain.board.code.*;
import io.github.wyatt.muffer.domain.board.entity.Board;
import io.github.wyatt.muffer.domain.board.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoardSvReq(
        @NotBlank(message = "제목은 필수입니다.") String title,
        Integer brandOptionId,
        String customBrand,
        @NotNull(message = "판매가격은 필수입니다.") int initialPrice,
        int exposureLevel,
        @NotBlank(message = "상품명은 필수입니다.") String productName,
        @NotBlank(message = "상품 카테고리는 필수입니다.") ProductCategory productCategory,
        String serialCode,
        @NotNull(message = "재판매 여부는 필수입니다.") boolean isSecondedHand,
        int usePeriod,
        @NotNull(message = "상품 상태는 필수입니다.")  boolean isDamaged,
        String brandName,
        String damageInfo,
        String driver,
        ChargeType chargeType,
        MicType micType,
        String connectType,
        float batteryTime,
        Boolean canWire,
        Boolean canWireless,
        QualityGrade qualityGrade
) {

    public Product extractProduct() {
        return Product.create(
                productName,
                productCategory,
                qualityGrade,
                brandOptionId,
                customBrand,
                serialCode,
                isSecondedHand,
                usePeriod,
                isDamaged,
                damageInfo,
                driver,
                chargeType,
                micType,
                connectType,
                batteryTime,
                canWire,
                canWireless
        );
    }

    public Board toEntity(Product product, int memberId) {
        return Board.create(
                title,
                BoardStatus.SELLING,
                product,
                initialPrice,
                exposureLevel,
                memberId
        );
    }

}

