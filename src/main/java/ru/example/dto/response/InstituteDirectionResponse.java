package ru.example.dto.response;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InstituteDirectionResponse {

    private String fullName;

    private String shortName;

    private InstituteResponse institute;
}
