package ru.example.dto.response.decanatAdditionalInfo;

import lombok.Data;

import java.util.List;


@Data
public class DecanatAdditionalInfo {

    private CountOfDiseasesByDays countOfDiseasesByDaysForTwoWeeks;

    private String countOfSickNow;

    private String countOfRecoverToday;

    private String countOfSickToday;

    private String directionWithHighestNumberOfDiseases;

    private List<DepartmentCountOfSick> departmentCountOfSicks;
}
