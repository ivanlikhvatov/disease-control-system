package ru.example.dto.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GroupDirectionResponse {

    private String name;

    private String index;
}
