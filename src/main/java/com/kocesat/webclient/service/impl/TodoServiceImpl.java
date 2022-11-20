package com.kocesat.webclient.service.impl;

import com.kocesat.webclient.model.Todo;
import com.kocesat.webclient.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoServiceImpl implements TodoService {

    private final WebClient todoWebClient;

    @Override
    public List<Todo> getTodoList() {
        long start = System.currentTimeMillis();
        final List<Todo> todos = todoWebClient.get()
                .retrieve()
                .bodyToFlux(Todo.class)
                .collectList()
                .block();
        long end = System.currentTimeMillis();
        log.info("Request took " + (end - start) + " milliseconds.");
        return todos;
    }

    @Override
    public Todo getTodoById(int id) {
        long start = System.currentTimeMillis();
        final Todo todos = todoWebClient.get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(Todo.class)
                .block();
        long end = System.currentTimeMillis();
        log.info("Request took " + (end - start) + " milliseconds.");
        return todos;
    }
}
