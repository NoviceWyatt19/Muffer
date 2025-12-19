package io.github.wyatt.muffer.domain.offer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity @Getter
public class Offer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;
    private Long bidderId;
    private Long counterOfferId;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;
    private String location;

    private int suggestPrice;

    @Builder
    private Offer(Long boardId, Long bidderId, Long counterOfferId, OfferStatus status, TradeType tradeType, String location, int suggestPrice) {
        this.boardId = boardId;
        this.bidderId = bidderId;
        this.counterOfferId = counterOfferId;
        this.status = status;
        this.tradeType = tradeType;
        this.location = location;
        this.suggestPrice = suggestPrice;
    }

    public static Offer create(Long boardId, Long bidderId, TradeType tradeType, String location, int suggestPrice) {
        return Offer.builder()
                .boardId(boardId)
                .bidderId(bidderId)
                .counterOfferId(null)
                .status(OfferStatus.PENDING)
                .tradeType(tradeType)
                .location(location)
                .suggestPrice(suggestPrice)
                .build();
    }

    public void markAsCounterOffer(Long counterOfferId) {
        this.counterOfferId = counterOfferId;
    }

    public void accept() {
        this.status = OfferStatus.ACCEPT;
    }

}
