package ru.example.dto.request.creationService;

import lombok.Data;
import ru.example.dao.entity.group.EducationLevel;
import ru.example.dao.entity.group.EducationType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GroupRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotNull
    private Integer course;

    @NotNull
    private EducationType educationType;

    @NotNull
    private EducationLevel educationLevel;

    @NotNull
    @Valid
    private DirectionProfileRequest directionProfile;

    @NotBlank
    private String serialNumber;
}
