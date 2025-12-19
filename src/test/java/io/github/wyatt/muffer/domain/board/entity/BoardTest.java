package io.github.wyatt.muffer.domain.board.entity;

import io.github.wyatt.muffer.domain.board.code.BoardStatus;
import io.github.wyatt.muffer.domain.board.code.ChargeType;
import io.github.wyatt.muffer.domain.board.code.ProductCategory;
import io.github.wyatt.muffer.domain.board.code.QualityGrade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    @DisplayName("create Board Entity")
    void createBoardTest(){
        Product product = Product.create(
                "test name",
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
        var board = Board.create(
                "test title",
                BoardStatus.SELLING,
                product,
                1000,
                3,
                1L
        );

        System.out.println(board.toString());
    }

}