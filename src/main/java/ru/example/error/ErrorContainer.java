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
    BAD_REQUEST(211, "Request validation error", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(212, "Пользователь не найден", HttpStatus.NOT_FOUND),
    USER_WITH_THIS_EMAIL_EXIST(213, "Данный e-mail уже используется", HttpStatus.BAD_REQUEST),
    USER_WITH_THIS_STUDENT_NUMBER_EXIST(214, "Пользователь с таким номером зачетки уже зарегистрирован", HttpStatus.BAD_REQUEST),
    ACTIVATION_CODE_NOT_FOUND(215, "Данного кода активации не существует", HttpStatus.NOT_FOUND),
    ACTIVATION_CODE_EXPIRED(216, "Срок действия кода активации истек", HttpStatus.FORBIDDEN),
    USER_ALREADY_CONFIRM_EMAIL(217, "Пользователь уже подтвердил почту", HttpStatus.NOT_FOUND),
    PAGE_NOT_FOUND(219, "Page not found", HttpStatus.NOT_FOUND),

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
