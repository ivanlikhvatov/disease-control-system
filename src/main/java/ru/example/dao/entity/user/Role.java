package ru.example.dao.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority {
    STUDENT("Студент"),
    CURATOR("Куратор"),
    CURATOR_SUPERVISING("Кафедрально ответственный"),
    RECTORAT("Ректорат"),
    DECANAT("Деканат"),
    TEACHER("Преподаватель"),
    ADMIN("Администратор");

    private final String description;

    @Override
    public String getAuthority() {
        return name();
    }

    @JsonCreator
    public static Role fromValue(String name) {
        return Arrays.stream(values())
                .filter(item -> item.description.equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.BAD_REQUEST));
    }
}
