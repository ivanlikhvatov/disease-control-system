package ru.example.error;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ApiError {

    private String message;

    private int errorCode;

    public ApiError(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}