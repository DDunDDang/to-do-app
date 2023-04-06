package com.codestates.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ToDoDto {
    @Getter
    public static class Post {
        private String title;
        private int todo_order;
        private boolean completed;
    }
    @Setter
    @Getter
    public static class Patch {
        private long id;
        private String title;
        private int todo_order;
        private boolean completed;
    }
    @Getter
    @Builder
    public static class Response {
        private long id;
        private String title;
        private int todo_order;
        private boolean completed;
    }
}
