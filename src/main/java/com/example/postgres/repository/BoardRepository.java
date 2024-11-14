package com.example.postgres.repository;

import com.example.postgres.model.Tables;
import com.example.postgres.model.tables.pojos.Board;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final DSLContext dsl;

    public List<Board> findAll() {
        return dsl.selectFrom(Tables.BOARD)
                .fetchInto(Board.class);
    }


    public Board findById(Long id) {
        return dsl.selectFrom(Tables.BOARD)
                .where(Tables.BOARD.NO.eq(id))
                .fetchOneInto(Board.class);
    }

    public int save(Board board) {
        return dsl.insertInto(Tables.BOARD)
                .set(dsl.newRecord(Tables.BOARD, board))
                .execute();
    }

    public int update(Board board) {
        return dsl.update(Tables.BOARD)
                .set(dsl.newRecord(Tables.BOARD, board))
                .where(Tables.BOARD.NO.eq(board.getNo()))
                .execute();
    }

    public int delete(Long id) {
        return dsl.deleteFrom(Tables.BOARD)
                .where(Tables.BOARD.NO.eq(id))
                .execute();
    }


}
