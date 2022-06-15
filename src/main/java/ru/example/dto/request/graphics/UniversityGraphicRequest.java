package ru.example.dto.request.graphics;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UniversityGraphicRequest {
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
