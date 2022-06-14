package ru.example.dto.request.creationService;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DepartmentRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String shortName;

    @NotBlank
    private String fullName;
}
