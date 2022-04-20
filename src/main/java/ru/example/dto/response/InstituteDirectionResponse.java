package ru.example.dto.response;

import lombok.Data;

@Data
public class InstituteDirectionResponse {

    private String id;

    private String fullName;

    private String shortName;

    private InstituteResponse institute;
}
