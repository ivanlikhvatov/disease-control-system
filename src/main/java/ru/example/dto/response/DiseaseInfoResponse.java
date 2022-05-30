package ru.example.dto.response;

import lombok.Data;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dto.request.disease.ApproveType;

import java.time.LocalDate;

@Data
public class DiseaseInfoResponse {

    private String id;

    private DiseaseResponse disease;

    private UserInfoDto user;

    private String otherDiseaseInformation;

    private LocalDate dateOfDisease;

    private LocalDate dateOfRecovery;

    private DiseaseStatus status;

    private ApproveType approveType;

    private String scannedCertificateInBase64;

    private String scannedCertificateFileName;

    private String electronicSickId;

    private String rejectCause;
}
