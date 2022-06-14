package ru.example.dto.request.graphics;

import lombok.Data;
import ru.example.dao.entity.user.Role;
import ru.example.dto.request.creationService.DepartmentRequest;
import ru.example.dto.request.creationService.InstituteRequest;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class InstituteGraphicRequest {

    @NotNull
    private InstituteRequest institute;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
