package io.github.wyatt.muffer.domain.offer;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.domain.board.code.ChargeType;
import io.github.wyatt.muffer.domain.board.code.ProductCategory;
import io.github.wyatt.muffer.domain.board.code.QualityGrade;
import io.github.wyatt.muffer.domain.board.entity.Board;
import io.github.wyatt.muffer.domain.board.entity.Product;
import io.github.wyatt.muffer.domain.board.repository.BoardRepo;
import io.github.wyatt.muffer.domain.board.repository.ProductRepo;
import io.github.wyatt.muffer.domain.offer.request.ChgStateReq;
import io.github.wyatt.muffer.domain.offer.request.OfrSvReq;
import io.github.wyatt.muffer.domain.offer.response.OfrListRes;
import io.github.wyatt.muffer.global.exceptions.BusinessAccessDeniedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OfferServiceTest {


    @Autowired
    private OfferService ofrService;

    @Autowired
    private ProductRepo prdRepo;

    @Autowired
    private BoardRepo brdRepo;

    @Autowired
    private OfferRepo ofrRepo;

    Product prd;
    Board brd;
    Offer ofr;

    @BeforeEach
    void setUp() {
        //given
        prd = Product.create(
                "test product name",
                ProductCategory.HEADPHONE,
                QualityGrade.A,
                1L,
                null,
                "serial code",
                false,
                4,
                false,
                null,
                "3.5mm",
                ChargeType.None,
                null,
                null,
                60.5,
                true,
                false
        );
        prdRepo.save(prd);
        brd = Board.create(
                "test board",
                BoardStatus.SELLING,
                prd,
                100,
                3,
                9L
        );
        brdRepo.save(brd);
        ofr = Offer.create(
                brd.getId(),
                1L,
                null,
                null,
                10000
        );
        ofrRepo.save(ofr);
    }

    @AfterEach
    void init() {
        ofrRepo.deleteAll();
    }

    @Test
    @DisplayName("Query Offer List")
    void getOfferListTest() {
        // when
        List<OfrListRes> res = ofrService.getOfferList(ofr.getBoardId(), null);

        // then
        assertThat(res.isEmpty(), is(false));
        assertThat(res.size(), is(1));
    }

    @Test
    @DisplayName("Can save offer")
    void saveOfferTest() {
        // given
        Long requesterId = 2L;
        OfrSvReq req = new OfrSvReq(TradeType.DELIVERY, "test location", 19000);

        //when
        ofrService.saveOffer(brd.getId(), 2L, req);

        //then
        assertThat(ofrService.getOfferList(ofr.getBoardId(), null).size(), is(2));
    }

    @Test
    @DisplayName("If not board creator, can not change state")
    void changeStateFailTest() {
        //when
        ChgStateReq req = new ChgStateReq(ofr.getId(), OfferStatus.ON_HOLD);
        //then
        assertThrows(BusinessAccessDeniedException.class, () -> ofrService.changeState(ofr.getBoardId(), 2L, req));
    }

    @Test
    @DisplayName("Changing offer state Can do only seller")
    void changeStateSuccessTest() {
        //given
        ChgStateReq req = new ChgStateReq(ofr.getId(), OfferStatus.ON_HOLD);
        //when
        ofrService.changeState(ofr.getBoardId(), brd.getMemberId(), req);
        //then
        assertThat(ofrRepo.findById(ofr.getId()).get().getStatus(), is(req.status()));
    }

}