package com.example.todosecurity.service;

import com.example.todosecurity.model.Todo;
import com.example.todosecurity.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService implements ITodoService{


        @Autowired
        private TodoRepository todoRepository;

        public Todo saveTodo(Todo todo) {
            return todoRepository.save(todo);
        }

        public List<Todo> getAllTodos() {
            return todoRepository.findAll();
        }

        public Optional<Todo> getTodoById(Long id) {
            return todoRepository.findById(id);
        }

        public Todo updateTodo(Todo todo) {
            return todoRepository.save(todo);
        }

        public void deleteTodo(Long id) {
            todoRepository.deleteById(id);
        }
        public Todo completeTodo(Long id) {
            Optional<Todo> todoOptional = todoRepository.findById(id);
            if (todoOptional.isPresent()) {
                Todo todo = todoOptional.get();
                todo.setCompleted(true);
                return todoRepository.save(todo);
            }
            return null;
        }

        public Todo inCompleteTodo(Long id) {
            Optional<Todo> todoOptional = todoRepository.findById(id);
            if (todoOptional.isPresent()) {
                Todo todo = todoOptional.get();
                todo.setCompleted(false);
                return todoRepository.save(todo);
            }
            return null;
        }
}

