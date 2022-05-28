package ru.example.dao.entity.disease;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DiseaseStatus {
    ACTIVE("Активно"),
    APPROVED_BY_SICK("Подтверждено больным"),
    PROCESSED("Обрабатывается"),
    CLOSED("Закрыто");

    private final String description;

}
