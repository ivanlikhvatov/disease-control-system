package ru.example.dto.response;

import lombok.Data;

@Data
public class DirectionProfileResponse {

    private String id;

    private InstituteDirectionResponse instituteDirection;

    private String name;

    private String index;
}
