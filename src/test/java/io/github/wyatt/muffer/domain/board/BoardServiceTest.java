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


    @BeforeEach
    void setUp() {
        Product product = Product.create(
                "test name",
                ProductCategory.HEADPHONE,
                QualityGrade.A,
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
        Board board = Board.create(
                "test title",
                BoardStatus.SELLING,
                product,
                1,
                "test brand name",
                1000,
                3
        );

        productRepo.save(product);
        boardRepo.save(board);
    }

    @Test
    @DisplayName("Find All Boards")
    void findAllTest() {
        BoardFilterReq req = new BoardFilterReq();
        List<BoardListRes> res = boardService.getBoards(req);
        System.out.println(res.toString());
        assertThat(res.isEmpty(), is(false));
        assertThat(res.size(), is(1));
    }

}