package ru.example.dto.response.graphics;

import lombok.Data;

@Data
public class DiseaseTypeCountOfSick {

    private String diseaseName;

    private Long countOfSick;
}
