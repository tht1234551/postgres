package com.example.postgres.repository;

import com.example.postgres.model.public_.tables.pojos.Board;
import com.example.postgres.model.public_.tables.records.BoardRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void contextLoads() {
        Assert.notNull(boardRepository, "boardRepository must not be null");
    }

    @Test
    void findAll() {

    }

    @Test
    void findById() {
    }

    @Test
    void save() {
        Board board = new Board(null, "test", "test", "test", null, null);
        int saveCount = boardRepository.save(board);
        assertEquals(saveCount, 1);
    }

    @Test
    void update() {


    }

    @Test
    void delete() {
    }
}