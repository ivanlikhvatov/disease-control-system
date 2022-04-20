package ru.example.dto.response;

import lombok.Data;
import ru.example.dao.entity.group.EducationLevel;
import ru.example.dao.entity.group.EducationType;

@Data
public class GroupResponse {

    private String id;

    private String name;

    private Integer course;

    private EducationType educationType;

    private EducationLevel educationLevel;

    private DirectionProfileResponse directionProfile;

    private String serialNumber;
}
