package com.example.postgres.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final DSLContext dsl;

    public void test() {
        System.out.println("dsl is " + null);
    }
    
}
