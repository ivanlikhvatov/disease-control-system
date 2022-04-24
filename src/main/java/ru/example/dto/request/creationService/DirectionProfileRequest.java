package ru.example.dto.request.creationService;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DirectionProfileRequest {
    @NotBlank
    private String id;

    @NotNull
    @Valid
    private InstituteDirectionRequest instituteDirection;

    @NotBlank
    private String name;

    @NotBlank
    private String index;
}
