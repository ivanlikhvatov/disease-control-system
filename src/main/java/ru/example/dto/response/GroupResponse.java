package ru.example.dto.response;

import lombok.Data;
import ru.example.dao.entity.group.EducationLevel;
import ru.example.dao.entity.group.EducationType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GroupResponse {

    private InstituteDirectionResponse instituteDirection;

    private Integer course;

    private EducationType educationType;

    private EducationLevel educationLevel;

    private GroupDirectionResponse groupDirection;

    private String serialNumber;
}
