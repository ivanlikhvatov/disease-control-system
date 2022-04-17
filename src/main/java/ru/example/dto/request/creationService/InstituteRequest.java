package ru.example.dto.request.creationService;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InstituteRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String shortName;
}
