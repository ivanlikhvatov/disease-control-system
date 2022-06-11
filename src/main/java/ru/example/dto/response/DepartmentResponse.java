package ru.example.dto.response;

import lombok.Data;

@Data
public class DepartmentResponse {
    private String id;

    private String shortName;

    private String fullName;
}
