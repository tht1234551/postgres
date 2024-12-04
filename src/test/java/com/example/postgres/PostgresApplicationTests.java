package com.example.postgres;

import com.example.postgres.model.public_.tables.records.AuthorRecord;
import com.example.postgres.model.public_.tables.records.BoardRecord;
import org.jooq.*;

import static com.example.postgres.model.information_schema.InformationSchema.INFORMATION_SCHEMA;
import static com.example.postgres.model.information_schema.Tables.COLUMNS;
import static java.util.stream.Collectors.*;
import static org.jooq.impl.DSL.*;

import org.jooq.*;
import org.jooq.impl.*;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.util.xml.jaxb.Column;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.example.postgres.model.public_.Tables.AUTHOR;
import static com.example.postgres.model.public_.Tables.BOOK;
import static com.example.postgres.model.public_.tables.Board.BOARD;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void fetchTest3() {
        // Use your favourite tool to construct SQL strings:
        AuthorRecord author = dsl.fetchOne(AUTHOR, AUTHOR.ID.eq(3));

        // Create a new author, if it doesn't exist yet
        if (author == null) {
            author = dsl.newRecord(AUTHOR);
            author.setId(3);
            author.setFirstName("Dan");
            author.setLastName("Brown");
        }

        // Mark the author as a "distinguished" author and store it
        author.setDistinguished(1);

        // Executes an update on existing authors, or insert on new ones
        author.store();
    }

    @Test
    void fetchTest4() {
        Result<?> result = dsl
                .select(
                        AUTHOR.FIRST_NAME,
                        AUTHOR.LAST_NAME,
                        BOOK.ID,
                        BOOK.TITLE
                )
                .from(AUTHOR)
                .join(BOOK)
                .on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
                .orderBy(BOOK.ID.asc())
                .fetch();

        System.out.println(result);
        assertEquals(4, result.size());
        assertEquals(Arrays.asList(1, 2, 3, 4), result.getValues(BOOK.ID));
    }

    @Test
    void tableTest() {
//        org.jooq.util.postgres.PostgresDSL.
        dsl
                .select(
                        COLUMNS.TABLE_NAME,
                        COLUMNS.COLUMN_NAME,
                        COLUMNS.DATA_TYPE
                )
                .from(COLUMNS)
                .orderBy(
                        COLUMNS.TABLE_CATALOG,
                        COLUMNS.TABLE_SCHEMA,
                        COLUMNS.TABLE_NAME,
                        COLUMNS.ORDINAL_POSITION
                )
                .fetch()  // jOOQ ends here
                .stream() // JDK 8 Streams start here
                .collect(groupingBy(
                        r -> r.getValue(COLUMNS.TABLE_NAME),
                        LinkedHashMap::new,
                        mapping(
                                r -> {
                                    Column column = new Column();
                                    column.setColumnName(r.getValue(COLUMNS.COLUMN_NAME));
                                    column.setDataType(r.getValue(COLUMNS.DATA_TYPE));
                                    return column;
                                },
                                toList()
                        )
                ))
                .forEach(
                        (table, columns) -> {
                            // Just emit a CREATE TABLE statement
                            System.out.println(
                                    "CREATE TABLE " + table + " (");

                            // Map each "Column" type into a String
                            // containing the column specification,
                            // and join them using comma and
                            // newline. Done!
                            System.out.println(
                                    columns.stream()
                                           .map(col -> "  " + col.getColumnName() +
                                                   " " + col.getDataType())
                                           .collect(Collectors.joining(",\n"))
                            );

                            System.out.println(");");
                        }
                );
    }

    @Test
    void asyncTest() {
        // Initiate an asynchronous call chain
        CompletableFuture

                // This lambda will supply an int value indicating the number of inserted rows
                .supplyAsync(() ->
                        dsl.insertInto(AUTHOR, AUTHOR.ID, AUTHOR.LAST_NAME)
                           .values(4, "Hitchcock")
                           .execute()
                )

                // This will supply an AuthorRecord value for the newly inserted author
                .handleAsync((rows, throwable) ->
                        dsl.fetchOne(AUTHOR, AUTHOR.ID.eq(4))
                )

                // This should supply an int value indicating the number of rows,
                // but in fact it'll throw a constraint violation exception
                .handleAsync((record, throwable) -> {
                    record.changed(true);
                    return record.insert();
                })

                // This will supply an int value indicating the number of deleted rows
                .handleAsync((rows, throwable) ->
                        dsl.delete(AUTHOR)
                           .where(AUTHOR.ID.eq(4))
                           .execute()
                )
                .join();
    }

    @Test
    void likeTest() {
        Result<Record> fetch = dsl.select()
                                  .from(BOOK)
                                  .where(BOOK.TITLE.like("Animal%"))
                                  .fetch();

        System.out.println(fetch);
    }
}
