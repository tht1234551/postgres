package com.example.postgres;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.util.List;

import static com.example.postgres.model.Tables.AUTHOR;
import static com.example.postgres.model.Tables.BOOK;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@SpringBootTest
class PostgresApplicationTests {

    @Autowired
    private DSLContext dsl;

    @Test
    void contextLoads() {
        Assert.notNull(dsl, "dsl must not be null");
    }

    @Test
    void makeQuery() {
        Query query = dsl.select(field("BOOK.TITLE"), field("AUTHOR.FIRST_NAME"), field("AUTHOR.LAST_NAME"))
                .from(table("BOOK"))
                .join(table("AUTHOR"))
                .on(field("BOOK.AUTHOR_ID").eq(field("AUTHOR.ID")))
                .where(field("BOOK.PUBLISHED_IN").eq(1948));
        String sql = query.getSQL(ParamType.INLINED);
        System.out.println("---------");
        System.out.println(sql);
        System.out.println("---------");
        List<Object> bindValues = query.getBindValues();
        bindValues.forEach(System.out::println);
        System.out.println("---------");
    }

    @Test
    void makeQuery2() {
        Query query = dsl.select(BOOK.TITLE, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                .from(BOOK)
                .join(AUTHOR)
                .on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                .where(BOOK.PUBLISHED_IN.eq(1948));

        String sql = query.getSQL(ParamType.INLINED);
        System.out.println("---------");
        System.out.println(sql);
        System.out.println("---------");
        List<Object> bindValues = query.getBindValues();
        bindValues.forEach(System.out::println);
        System.out.println("---------");
    }

    @Test
    void fetchTest() {
        Result<Record3<String, String, String>> result =
                dsl.select(BOOK.TITLE, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                        .from(BOOK)
                        .join(AUTHOR)
                        .on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                        .where(BOOK.PUBLISHED_IN.eq(1948))
                        .fetch();

        System.out.println(result);
    }

    @Test
    void fetchTest2() {
        // Use your favourite tool to construct SQL strings:
        String sql = "SELECT title, first_name, last_name FROM book JOIN author ON book.author_id = author.id " +
                "WHERE book.published_in = 1948";

        // Fetch results using jOOQ
        Result<Record> result = dsl.fetch(sql);
        System.out.println(result);
    }


}
