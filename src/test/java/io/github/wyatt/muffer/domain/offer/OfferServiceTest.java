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
        List<OfrListRes> res = ofrService.getOfferList(ofr.getBoardId(), null);
        System.out.println(res.getFirst().toString());
        assertThat(res.isEmpty(), is(false));
        assertThat(res.size(), is(1));
    }

    @Test
    @DisplayName("Can save offer")
    void saveOfferTest() {
        Offer newOffer = Offer.create(
                ofr.getBoardId(),
                2L,
                null,
                null,
                10
        );
        ofrService.saveOffer(newOffer);
        assertThat(ofrService.getOfferList(ofr.getBoardId(), null).size(), is(2));
    }

    @Test
    @DisplayName("If not board creator, can not change state")
    void changeStateFailTest() {
        ChgStateReq req = new ChgStateReq(ofr.getId(), OfferStatus.ON_HOLD);
        assertThrows(BusinessAccessDeniedException.class, () -> ofrService.changeState(ofr.getBoardId(), 2L, req));
    }

    @Test
    @DisplayName("Changing offer state Can do only seller")
    void changeStateSuccessTest() {
        ChgStateReq req = new ChgStateReq(ofr.getId(), OfferStatus.ON_HOLD);
        ofrService.changeState(ofr.getBoardId(), brd.getMemberId(), req);
        Optional<Offer> offer = ofrRepo.findById(ofr.getId());
        assertThat(offer.get().getStatus(), is(req.status()));
    }

}