package com.kocesat.webclient.service;

import com.kocesat.webclient.model.Todo;

import java.util.List;

public interface TodoService {

    List<Todo> getTodoList();

    Todo getTodoById(int id);

}
