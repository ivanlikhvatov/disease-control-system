package ru.example.dto.response.graphics;

import lombok.Data;

import java.util.List;

@Data
public class GroupGraphicInfo {
    private CountOfDiseasesByDays countOfDiseasesByDays;

    private List<UniversityPartCountOfSick> universityPartCountOfSicks;

    private List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks;
}
