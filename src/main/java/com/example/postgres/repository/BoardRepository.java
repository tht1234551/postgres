package com.example.postgres.repository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final DSLContext dsl;


    
}
