package io.github.wyatt.muffer.domain.board;

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

    //TODO memberId 적용
    @Transactional
    public void saveBoard(BoardSvReq req, int memberId) {
        log.debug("save board info -> {}", req.toString());
        var product = req.extractProduct();
        pdtRepo.save(product);
        brdRepo.save(
                req.toEntity(product, memberId)
        );
    }

}
