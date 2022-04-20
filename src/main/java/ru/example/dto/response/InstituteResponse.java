package ru.example.dto.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class InstituteResponse {

    private String id;

    private String fullName;

    private String shortName;
}
