package ru.example.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiseaseInfoResponse {

    private String id;

    private DiseaseResponse disease;

    private String otherDiseaseInformation;

    private LocalDate dateOfDisease;

    private LocalDate dateOfRecovery;

    private Boolean isApproved;

    private Boolean isClosed;
}
