package ru.example.dto.response.decanatAdditionalInfo;

import lombok.Data;


@Data
public class DecanatAdditionalInfo {

    private CountOfDiseasesByDays countOfDiseasesByDaysForTwoWeeks;

    private String countOfSickToday;

    private String directionWithHighestNumberOfDiseases;
}
