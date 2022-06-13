package ru.example.dto.request.graphics;

import lombok.Data;
import ru.example.dto.request.creationService.GroupRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class GroupGraphicRequest {

    @NotNull
    private GroupRequest group;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
