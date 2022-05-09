package ru.example.dto.request.disease;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DiseaseRequest {
    @NotBlank
    private String id;
}
