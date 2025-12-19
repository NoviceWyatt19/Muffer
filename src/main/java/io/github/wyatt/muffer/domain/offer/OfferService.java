package io.github.wyatt.muffer.domain.offer;

import io.github.wyatt.muffer.domain.board.BoardService;
import io.github.wyatt.muffer.domain.offer.request.ChgStateReq;
import io.github.wyatt.muffer.domain.offer.request.OfrFilterReq;
import io.github.wyatt.muffer.domain.offer.response.OfrListRes;
import io.github.wyatt.muffer.global.config.ErrorCode;
import io.github.wyatt.muffer.global.exceptions.BusinessNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfferService {

    private final BoardService brdService;
    private final OfferRepo ofrRepo;

    @Transactional
    public void saveOffer(Offer offer) {
        ofrRepo.save(offer);
        log.info("save offer -> {}", offer);
    }

    public List<OfrListRes> getOfferList(Long boardId, OfrFilterReq req) {
        log.info("find all of offer in boardId({})", boardId);
        return ofrRepo.findList();
    }

    public void changeState(Long boardId, Long requesterId, ChgStateReq req) {
        brdService.validateSeller(boardId, requesterId);
        log.info("success validation.");
        Offer offer = ofrRepo.findById(req.offerId())
                .orElseThrow(() -> new BusinessNotFoundException(ErrorCode.NOT_FOUND_DATA));
        offer.changeState(req.status());
    }
}
