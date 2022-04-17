package ru.example.dao.entity.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EducationLevel {
    UNDERGRADUATE("Бакалавриат", "б"),
    MAGISTRACY("Магистратура", "м"),
    SPECIALTY("Специалитет", "с"),
    POSTGRADUATE("Аспирантура", "а");

    private final String valueFromClient;
    private final String shortName;

    @JsonCreator
    public static EducationLevel fromValue(String name) {
        return Arrays.stream(values())
                .filter(item -> item.valueFromClient.equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorContainer.BAD_REQUEST));
    }
}
