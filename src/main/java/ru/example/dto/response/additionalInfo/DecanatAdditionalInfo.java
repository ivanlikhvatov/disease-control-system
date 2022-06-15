package ru.example.dto.response.additionalInfo;

import lombok.Data;
import ru.example.dto.response.graphics.CountOfDiseasesByDays;
import ru.example.dto.response.graphics.UniversityPartCountOfSick;
import ru.example.dto.response.graphics.DiseaseTypeCountOfSick;

import java.util.List;


@Data
public class DecanatAdditionalInfo {

    private CountOfDiseasesByDays countOfDiseasesByDaysForTwoWeeks;

    private String countOfSickNow;

    private String countOfRecoverToday;

    private String countOfSickToday;

    private List<UniversityPartCountOfSick> universityPartCountOfSicks;

    private List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks;

}
