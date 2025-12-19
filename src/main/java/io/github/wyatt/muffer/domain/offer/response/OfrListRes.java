package io.github.wyatt.muffer.domain.offer.response;

import io.github.wyatt.muffer.domain.offer.Offer;
import io.github.wyatt.muffer.domain.offer.OfferStatus;
import io.github.wyatt.muffer.domain.offer.TradeType;

public record OfrListRes(
    Long offerId,
    Long boardId,
    Long bidderId,
    Long counterOfferId,
    OfferStatus status,
    TradeType tradeType,
    String location,
    int suggestPrice
) {
    public static OfrListRes from(Offer offer) {
        return new OfrListRes(
                offer.getId(),
                offer.getBoardId(),
                offer.getBidderId(),
                offer.getCounterOfferId(),
                offer.getStatus(),
                offer.getTradeType(),
                offer.getLocation(),
                offer.getSuggestPrice()
        );
    }
}
