package com.codestates.todo.mapper;

import com.codestates.todo.dto.ToDoDto;
import com.codestates.todo.entity.ToDo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ToDoMapper {
    @Mapping(target = "toDoOrder", source = "post.todo_order")
    ToDo toDoPostToToDo(ToDoDto.Post post);
    @Mapping(target = "toDoOrder", source = "patch.todo_order")
    ToDo toDoPatchToToDo(ToDoDto.Patch patch);
    @Mapping(target = "todo_order", source = "toDo.toDoOrder")
    ToDoDto.Response toDoToToDoResponse(ToDo toDo);
    @Mapping(target = "todo_order", source = "toDo.toDoOrder")
    List<ToDoDto.Response> toDosToToDoResponses(List<ToDo> toDos);
}
