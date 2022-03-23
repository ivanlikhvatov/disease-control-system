package ru.example.dao.entity.user;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("муж"),
    FEMALE("жен");

    private final String valueFromVueClient;

    @JsonCreator
    public static Gender fromValue(String name) {
        return Arrays.stream(values())
                .filter(item -> item.valueFromVueClient.equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.BAD_REQUEST));
    }
}
