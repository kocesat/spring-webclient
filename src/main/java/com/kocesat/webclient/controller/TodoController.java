package com.kocesat.webclient.controller;

import com.kocesat.webclient.model.Todo;
import com.kocesat.webclient.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<Object> getTodos() {
        return ResponseEntity.ok(todoService.getTodoList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTodoById(@PathVariable int id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @GetMapping("/upto/{id}")
    public ResponseEntity<Object> getTodoUptoById(@PathVariable int id) {
        List<Todo> todos = new ArrayList<>();
        for (int i = 1; i <= id; i++) {
            todos.add(todoService.getTodoById(i));
        }
        return ResponseEntity.ok(todos);
    }
}
