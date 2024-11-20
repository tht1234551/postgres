package com.example.postgres.repository;

import static com.example.postgres.model.public_.Tables.BOARD;
import com.example.postgres.model.public_.tables.pojos.Board;
import com.example.postgres.model.public_.tables.records.BoardRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final DSLContext dsl;

    public List<Board> findAll() {
        return dsl.selectFrom(BOARD)
                .fetchInto(Board.class);
    }


    public Board findById(Long id) {
        return dsl.selectFrom(BOARD)
                .where(BOARD.NO.eq(id))
                .fetchOneInto(Board.class);
    }

    public int save(Board board) {
                return dsl.insertInto(BOARD)
                .set(dsl.newRecord(BOARD, board))
                .execute();

//        return dsl.insertInto(BOARD)
//                .columns(BOARD.TITLE, BOARD.CONTENT, BOARD.WRITER)
//                .
//                .values(board.getTitle(), board.getContent(), board.getWriter())
//                .execute();


//        return dsl.insertInto(BOARD)
//                .set(dsl.newRecord(BOARD, board))
//                .execute();
    }

//    public int save1(Board board) {
//
//        dsl.insertInto(BOARD, BOARD.TITLE, BOARD.CONTENT, BOARD.WRITER)
//
//        BoardRecord boardRecord = dsl.newRecord(BOARD);
//        boardRecord.setContent(board.getContent());
//        return 0;
//    }

    public int update(Board board) {
        return dsl.update(BOARD)
                .set(dsl.newRecord(BOARD, board))
                .where(BOARD.NO.eq(board.getNo()))
                .execute();
    }

    public int delete(Long id) {
        return dsl.deleteFrom(BOARD)
                .where(BOARD.NO.eq(id))
                .execute();
    }


}
