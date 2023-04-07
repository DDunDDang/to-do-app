package com.codestates.todo.repository;

import com.codestates.todo.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
}
