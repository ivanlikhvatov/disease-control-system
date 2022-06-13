package ru.example.dto.response.graphics;

import lombok.Data;

import java.util.List;

@Data
public class CountOfDiseasesByDays {
    private List<String> dates;
    private List<Long> countsOfSick;
}
