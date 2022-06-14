package ru.example.dto.response.graphics;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentGraphicInfo {
    private CountOfDiseasesByDays countOfDiseasesByDays;

    private List<UniversityPartCountOfSick> universityPartCountOfSicks;

    private List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks;
}
