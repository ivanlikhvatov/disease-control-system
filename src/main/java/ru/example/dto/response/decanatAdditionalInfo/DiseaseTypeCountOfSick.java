package ru.example.dto.response.decanatAdditionalInfo;

import lombok.Data;

@Data
public class DiseaseTypeCountOfSick {

    private String diseaseName;

    private Long countOfSick;
}
