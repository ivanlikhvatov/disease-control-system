package ru.example.dao.entity.disease;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DiseaseStatus {
    ACTIVE("Активно"),
    APPROVED("Подтверждено"),
    PROCESSED("Обрабатывается"),
    REJECTED("Отклонено");

    private final String description;

}
