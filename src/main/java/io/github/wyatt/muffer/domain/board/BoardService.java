package io.github.wyatt.muffer.domain.board;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.domain.board.entity.Board;
import io.github.wyatt.muffer.domain.board.repository.BoardRepo;
import io.github.wyatt.muffer.domain.board.repository.ProductRepo;
import io.github.wyatt.muffer.domain.board.request.BoardFilterReq;
import io.github.wyatt.muffer.domain.board.request.BoardSvReq;
import io.github.wyatt.muffer.domain.board.response.BoardListRes;
import io.github.wyatt.muffer.domain.option.OptionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepo brdRepo;
    private final ProductRepo pdtRepo;
    private  final OptionRepo optRepo;

    public List<BoardListRes> getBoards(BoardFilterReq res) {
        List<Board> boards = brdRepo.findAll();

        return boards.stream().map( (board) -> BoardListRes.from(board, getBrand(board.getProduct().getBrandOptionId(),  board.getProduct().getCustomBrand())) ).toList();
    }

    private String getBrand(Integer brandId, String customBrand) {

        String brandName = optRepo.findNameById(brandId).orElse(customBrand);
        log.info("brand name -> {}", brandName);
        return brandName;
    }

    @Transactional
    public void saveBoard(BoardSvReq req, int memberId) {
        log.debug("save board info -> {}", req.toString());
        var product = req.extractProduct();
        pdtRepo.save(product);
        brdRepo.save(
                req.toEntity(product, memberId)
        );
    }

    @Transactional
    public void updateState(int boardId, BoardStatus state) {
        Board board = brdRepo.findById(boardId).orElseThrow();
        board.changeStatus(state);
        log.info("board id{}: update state -> {}", boardId, state);
    }
}
