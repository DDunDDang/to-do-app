package com.codestates.data;

import com.codestates.todo.dto.ToDoDto;
import com.codestates.todo.entity.ToDo;

import java.util.List;

public class StubData {

    public static String getCreateContent() {
        return "{\n" +
                "\"title\": \"테스트하기\",\n" +
                "\"todo_order\": 1,\n" +
                "\"completed\": true\n" +
                "}";
    }

    public static String getPatchContent() {
        return "{\n" +
                "    \"id\" : \"1\",\n" +
                "    \"title\" : \"테스트하기\",\n" +
                "    \"todo_order\" : 1,\n" +
                "    \"completed\" : true\n" +
                "}";
    }

    public static List<ToDo> getToDoList() {
        ToDo toDo1 = new ToDo();
        toDo1.setId(1);
        toDo1.setTitle("테스트하기");
        toDo1.setToDoOrder(1);
        toDo1.setCompleted(true);

        ToDo toDo2 = new ToDo();
        toDo2.setId(2);
        toDo2.setTitle("잠자기");
        toDo2.setToDoOrder(2);
        toDo2.setCompleted(false);

        return List.of(toDo1, toDo2);
    }
    public static ToDoDto.Response getResponse() {
        return ToDoDto.Response.builder()
                .id(1)
                .title("테스트하기")
                .todo_order(1)
                .completed(true)
                .build();
    }

    public static List<ToDoDto.Response> getResponses() {
        return List.of(
        ToDoDto.Response.builder()
                .id(1)
                .title("테스트하기")
                .todo_order(1)
                .completed(true)
                .build(),
        ToDoDto.Response.builder()
                .id(2)
                .title("잠자기")
                .todo_order(2)
                .completed(false)
                .build());
    }
}
