package io.github.wyatt.muffer.domain.offer.request;

import io.github.wyatt.muffer.domain.offer.OfferStatus;

public record ChgStateReq(
        Long offerId,
        OfferStatus status
) {
}
