package io.github.wyatt.muffer.domain.board;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.domain.board.code.ChargeType;
import io.github.wyatt.muffer.domain.board.code.ProductCategory;
import io.github.wyatt.muffer.domain.board.code.QualityGrade;
import io.github.wyatt.muffer.domain.board.entity.Board;
import io.github.wyatt.muffer.domain.board.entity.Product;
import io.github.wyatt.muffer.domain.board.repository.BoardRepo;
import io.github.wyatt.muffer.domain.board.repository.ProductRepo;
import io.github.wyatt.muffer.domain.board.request.BoardFilterReq;
import io.github.wyatt.muffer.domain.board.response.BoardListRes;
import io.github.wyatt.muffer.domain.option.Option;
import io.github.wyatt.muffer.domain.option.OptionRepo;
import io.github.wyatt.muffer.domain.option.OptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepo boardRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    OptionRepo optionRepo;

    Option option;
    Product product;
    Board board;


    @BeforeEach
    void setUp() {
        option = Option.create(OptionType.BRAND, "test brand");
        product = Product.create(
                "test product name",
                ProductCategory.HEADPHONE,
                QualityGrade.A,
                1,
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
                60.5f,
                true,
                false
        );
        board = Board.create(
                "test title",
                BoardStatus.SELLING,
                product,
                1000,
                3,
                9
        );
        optionRepo.save(option);
        productRepo.save(product);
        boardRepo.save(board);
    }

    //TODO 현재 문제: 1. createdAt이 null
    @Test
    @DisplayName("Find All Boards")
    void findAllTest() {
        BoardFilterReq req = new BoardFilterReq();
        List<BoardListRes> res = boardService.getBoards(req);
        System.out.println(res.toString());
        assertThat(res.isEmpty(), is(false));
        assertThat(res.size(), is(1));
        assertThat(res.getFirst().productBrand(), is(option.getName()));
        assertThat(res.getFirst().title(), is(board.getTitle()));
        assertThat(res.getFirst().productName(), is(product.getName()));
    }

}