package ru.example.dto.response;

import lombok.Data;
import ru.example.dao.entity.disease.DiseaseStatus;

import java.time.LocalDate;

@Data
public class DiseaseInfoResponse {

    private String id;

    private DiseaseResponse disease;

    private String otherDiseaseInformation;

    private LocalDate dateOfDisease;

    private LocalDate dateOfRecovery;

    private DiseaseStatus status;
}
