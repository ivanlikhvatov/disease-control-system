package ru.example.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorContainer {

    AUTHENTICATION_ERROR(210, "Authentication error", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST(211, "Bad request", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(212, "User not found", HttpStatus.NOT_FOUND),
    OTHER(999, "Other Type of Error. See error message", HttpStatus.INTERNAL_SERVER_ERROR);

    private static final ErrorContainer[] VALUES = values();

    private final int code;

    private final String message;

    private final HttpStatus httpStatus;

    @Override
    public String toString() {
        return "ErrorContainer{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }

    public static ErrorContainer of(int errorCode) {
        return Arrays.stream(VALUES)
                .filter(errorContainer ->
                        errorContainer.getCode() == errorCode)
                .findFirst()
                .orElse(null);
    }
}
