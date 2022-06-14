package ru.example.dto.request.graphics;

import lombok.Data;
import ru.example.dto.request.creationService.DepartmentRequest;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class DepartmentGraphicRequest {

    @NotNull
    private DepartmentRequest department;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
