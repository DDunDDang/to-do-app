package com.codestates.todo.controller;

import com.codestates.todo.dto.ToDoDto;
import com.codestates.todo.entity.ToDo;
import com.codestates.todo.mapper.ToDoMapper;
import com.codestates.todo.service.ToDoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("/")
public class ToDoController {
    private final static String TODO_DEFAILT_URL = "/";
    private final ToDoService toDoService;
    private final ToDoMapper mapper;

    public ToDoController(ToDoService toDoService, ToDoMapper mapper) {
        this.toDoService = toDoService;
        this.mapper = mapper;
    }
    @PostMapping
    public ResponseEntity postToDo(@RequestBody ToDoDto.Post requestBody) {
        ToDo toDo = mapper.toDoPostToToDo(requestBody);

        ToDo createdToDo = toDoService.createToDo(toDo);
        URI location = UriComponentsBuilder
                .newInstance()
                .path(TODO_DEFAILT_URL + "{id}")
                .buildAndExpand(createdToDo.getId())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(mapper.toDoToToDoResponse(createdToDo));
    }
    @PatchMapping("{todo-id}")
    public ResponseEntity patchToDo(@PathVariable("todo-id") int toDoId, @RequestBody ToDoDto.Patch requestBody) {
        requestBody.setId(toDoId);

        ToDo toDo = toDoService.updateToDo(mapper.toDoPatchToToDo(requestBody));

        return new ResponseEntity<>(mapper.toDoToToDoResponse(toDo), HttpStatus.OK);
    }
    @GetMapping("{todo-id}")
    public ResponseEntity getToDo(@PathVariable("todo-id") int toDoId) {
        ToDo toDo = toDoService.findToDo(toDoId);

        return new ResponseEntity<>(mapper.toDoToToDoResponse(toDo), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity getToDos() {
        return new ResponseEntity<>(mapper.toDosToToDoResponses(toDoService.findToDos()), HttpStatus.OK);
    }
    @DeleteMapping("{todo-id}")
    public ResponseEntity deleteToDo(@PathVariable("todo-id") int toDoId) {
        toDoService.deleteToDo(toDoId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping
    public ResponseEntity deletesToDo() {
        toDoService.deleteToDos();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
