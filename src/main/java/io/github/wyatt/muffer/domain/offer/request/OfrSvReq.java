package io.github.wyatt.muffer.domain.offer.request;

import io.github.wyatt.muffer.domain.offer.Offer;
import io.github.wyatt.muffer.domain.offer.TradeType;

public record OfrSvReq(
        TradeType tradeType,
        String location,
        Integer suggestPrice
) {
    public Offer toEntity(Long boardId, Long bidderId) {
        return Offer.create(
                boardId,
                bidderId,
                this.tradeType,
                this.location,
                this.suggestPrice
        );
    }
}
