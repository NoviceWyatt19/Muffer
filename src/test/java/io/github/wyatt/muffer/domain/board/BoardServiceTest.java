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
import io.github.wyatt.muffer.global.exceptions.BusinessAccessDeniedException;
import io.github.wyatt.muffer.global.exceptions.ForbiddenModifyException;
import org.junit.jupiter.api.*;
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
//@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
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
        optionRepo.save(option);
        product = Product.create(
                "test product name",
                ProductCategory.HEADPHONE,
                QualityGrade.A,
                option.getId(),
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
        productRepo.save(product);
        board = Board.create(
                "test title",
                BoardStatus.SELLING,
                product,
                1000,
                3,
                9L
        );
        boardRepo.save(board);
    }

    @AfterEach
    void init() {
        boardRepo.deleteAll();
        productRepo.deleteAll();
        optionRepo.deleteAll();
    }

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
        Board checkBoard = boardRepo.findById(board.getId()).get();
    }

    @Test
    @DisplayName("Can not find hidden board")
    void findHiddenBoardTest() {
        boardService.updateState(board.getMemberId(), board.getId(), BoardStatus.HIDDEN);
        List<BoardListRes> res = boardService.getBoards(new BoardFilterReq());
        assertThat(res.isEmpty(), is(true));
    }

    @Test
    @DisplayName("Update Board State")
    void updateStateTest() {
        boardService.updateState(board.getMemberId(), board.getId(), BoardStatus.DEAL_AGREED);
        Board updatedBoard = boardRepo.findById(board.getId()).get();
        assertThat(updatedBoard.getStatus(), is(BoardStatus.DEAL_AGREED));
    }

    @Test
    @DisplayName("Can not modify state from SOLD_OUT or DEAL_AGREED")
    void forbiddenModifyStateTest() {
        boardService.updateState(board.getMemberId(), board.getId(), BoardStatus.SOLD_OUT);
        assertThrows(ForbiddenModifyException.class, () -> boardService.updateState(board.getMemberId(), board.getId(), BoardStatus.DEAL_AGREED));
    }

    @Test
    @DisplayName("Can not modify board state from other member")
    void forbiddenModifyStateFromOtherMemberTest() {
        assertThrows(BusinessAccessDeniedException.class, () -> boardService.updateState(1L, board.getId(), BoardStatus.HIDDEN));
    }

}