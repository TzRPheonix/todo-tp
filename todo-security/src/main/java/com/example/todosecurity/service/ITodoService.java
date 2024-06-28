package com.example.todosecurity.service;

import com.example.todosecurity.model.Todo;

import java.util.List;
import java.util.Optional;

public interface ITodoService {
    Todo saveTodo(Todo todo);

    List<Todo> getAllTodos();

    Optional<Todo> getTodoById(Long id);

    Todo updateTodo(Todo todo);

    void deleteTodo(Long id);

    Todo completeTodo(Long id);

    Todo inCompleteTodo(Long id);
}