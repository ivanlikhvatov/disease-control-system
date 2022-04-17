package ru.example.dto.request.creationService;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InstituteDirectionRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String shortName;

    @NotNull
    @Valid
    private InstituteRequest institute;
}
