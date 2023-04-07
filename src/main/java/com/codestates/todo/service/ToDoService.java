package com.codestates.todo.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.todo.entity.ToDo;
import com.codestates.todo.repository.ToDoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToDoService {
    private final ToDoRepository toDoRepository;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public ToDo createToDo(ToDo toDo) {
        return toDoRepository.save(toDo);
    }

    public ToDo updateToDo(ToDo toDo) {
        ToDo findedToDo = findVerifiedToDo(toDo.getId());

        Optional.ofNullable(toDo.getTitle())
                .ifPresent(toDoTitle -> findedToDo.setTitle(toDoTitle));
        Optional.ofNullable(toDo.getToDoOrder())
                .ifPresent(toDoOrder -> findedToDo.setToDoOrder(toDoOrder));
        findedToDo.setCompleted(toDo.isCompleted());

        return toDoRepository.save(findedToDo);
    }

    public ToDo findToDo(int toDoId) {
        ToDo findedToDo = findVerifiedToDo(toDoId);

        return findedToDo;
    }

    public List<ToDo> findToDos() {
        return toDoRepository.findAll(Sort.by("toDoOrder").ascending());
    }

    public void deleteToDo(int toDoId) {
        toDoRepository.deleteById(toDoId);
    }

    public void deleteToDos() {
        toDoRepository.deleteAll();
    }

    private ToDo findVerifiedToDo(int toDoId) {
        Optional<ToDo> toDo = toDoRepository.findById(toDoId);
        ToDo findToto = toDo.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.TODO_NOT_FOUND));
        return findToto;
    }
}
