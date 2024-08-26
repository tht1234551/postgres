package com.example.postgres.controller;

import com.example.postgres.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class BoardRestController {
    private final BoardRepository boardRepository;

    @GetMapping
    public void test() {
        System.out.println(boardRepository == null);
        System.out.println("test");
    }
}
