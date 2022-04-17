package ru.example.dto.request.creationService;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GroupDirectionRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String index;
}
