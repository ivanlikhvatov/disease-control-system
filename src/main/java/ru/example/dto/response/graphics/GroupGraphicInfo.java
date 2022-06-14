package ru.example.dto.response.graphics;

import lombok.Data;

import java.util.List;

@Data
public class GroupGraphicInfo {
    private CountOfDiseasesByDays countOfDiseasesByDaysInGroup;

    private List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicksInGroup;

    private List<UniversityPartCountOfSick> groupsCountOfSicks;
}
