package ru.example.dto.request.creationService;

import lombok.Data;
import ru.example.dao.entity.group.EducationLevel;
import ru.example.dao.entity.group.EducationType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GroupRequest {

    @NotNull
    @Valid
    private InstituteDirectionRequest instituteDirection;

    @NotNull
    private Integer course;

    @NotNull
    private EducationType educationType;

    @NotNull
    private EducationLevel educationLevel;

    @NotNull
    @Valid
    private GroupDirectionRequest groupDirection;

    @NotBlank
    private String serialNumber;
}
