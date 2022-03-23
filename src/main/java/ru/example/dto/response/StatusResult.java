package ru.example.dto.response;

import lombok.Data;

@Data
public class StatusResult {

    public static final String DEFAULT_SUCCESS_MESSAGE = "ok";

    private String status;

    private String message;

    public StatusResult() {
    }

    public StatusResult(String status) {
        this.status = status;
    }

    public StatusResult(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static StatusResult ok() {
        return new StatusResult(DEFAULT_SUCCESS_MESSAGE);
    }
}
