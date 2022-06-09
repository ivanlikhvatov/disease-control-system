package ru.example.dto.response.decanatAdditionalInfo;

import lombok.Data;

import java.util.List;

@Data
public class DecanatAdditionalInfoResponse {
    private CountOfDiseasesByDays countOfDiseasesByDaysForTwoWeeks;

    private String countOfSickToday;

    private String directionWithHighestNumberOfDiseases;
}
