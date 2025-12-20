package io.github.wyatt.muffer.domain.offer;

import io.github.wyatt.muffer.domain.offer.response.OfrListRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepo extends JpaRepository<Offer, Long> {

    @Query("select new io.github.wyatt.muffer.domain.offer.response." +
            "OfrListRes(o.id, o.boardId, o.bidderId, o.counterOfferId, o.status, o.tradeType, o.location, o.suggestPrice) from Offer o")
    List<OfrListRes> findList();

}
