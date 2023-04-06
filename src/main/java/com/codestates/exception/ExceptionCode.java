package com.codestates.exception;

import lombok.Getter;

public enum ExceptionCode {
    TODO_NOT_FOUND(404, "ToDo not found");
    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
