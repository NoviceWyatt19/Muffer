package io.github.wyatt.muffer.domain.board;

import io.github.wyatt.muffer.domain.board.entity.Board;
import io.github.wyatt.muffer.domain.board.repository.BoardRepo;
import io.github.wyatt.muffer.domain.board.repository.ProductRepo;
import io.github.wyatt.muffer.domain.board.request.BoardFilterReq;
import io.github.wyatt.muffer.domain.board.request.BoardSvReq;
import io.github.wyatt.muffer.domain.board.response.BoardListRes;
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

    private final BoardRepo bRepo;
    private final ProductRepo pRepo;

    public List<BoardListRes> getBoards(BoardFilterReq res) {
        List<Board> boards = bRepo.findAll();
        return boards.stream().map(BoardListRes::from).toList();
    }

    //TODO memberId 적용
    @Transactional
    public void saveBoard(BoardSvReq req, int memberId) {
        log.debug("save board info -> {}", req.toString());
        var product = req.extractProduct();
        pRepo.save(product);
        bRepo.save(
                req.toEntity(product)
        );
    }

}
