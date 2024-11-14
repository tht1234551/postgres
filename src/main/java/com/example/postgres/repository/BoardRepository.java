package com.example.postgres.repository;

import com.example.postgres.model.Tables;
import com.example.postgres.model.tables.pojos.TblBoard;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final DSLContext dsl;

    public List<TblBoard> findAll() {
        return dsl.selectFrom(Tables.TBL_BOARD)
                .fetchInto(TblBoard.class);
    }


    public TblBoard findById(Long id) {
        return dsl.selectFrom(Tables.TBL_BOARD)
                .where(Tables.TBL_BOARD.NO.eq(id))
                .fetchOneInto(TblBoard.class);
    }

    public int save(TblBoard board) {
        return dsl.insertInto(Tables.TBL_BOARD)
                .set(dsl.newRecord(Tables.TBL_BOARD, board))
                .execute();
    }

    public int update(TblBoard board) {
        return dsl.update(Tables.TBL_BOARD)
                .set(dsl.newRecord(Tables.TBL_BOARD, board))
                .where(Tables.TBL_BOARD.NO.eq(board.getId()))
                .execute();
    }

    public int delete(Long id) {
        return dsl.deleteFrom(Tables.TBL_BOARD)
                .where(Tables.TBL_BOARD.NO.eq(id))
                .execute();
    }


}
