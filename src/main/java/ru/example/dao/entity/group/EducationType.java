package ru.example.dao.entity.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EducationType {
    FULL_TIME("очная"),
    PART_TIME("заочная");

    private final String valueFromClient;

    @JsonCreator
    public static EducationType fromValue(String name) {
        return Arrays.stream(values())
                .filter(item -> item.valueFromClient.equals(name) || item.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.BAD_REQUEST));
    }
}
