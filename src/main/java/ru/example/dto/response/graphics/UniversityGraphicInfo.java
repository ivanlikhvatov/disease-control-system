package ru.example.dto.response.graphics;

import lombok.Data;

import java.util.List;

@Data
public class UniversityGraphicInfo {
    private CountOfDiseasesByDays countOfDiseasesByDays;

    private List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks;
}
