package ru.example.service;

import ru.example.dto.request.disease.ApproveDiseaseRequest;
import ru.example.dto.request.disease.AddDiseaseInformationRequest;
import ru.example.dto.request.disease.EditDiseaseInformationRequest;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.DiseaseResponse;
import ru.example.dto.response.StatusResult;
import ru.example.security.jwt.JwtUser;

import java.util.List;

public interface DiseaseService {
    List<DiseaseResponse> getDiseases();

    StatusResult addDiseaseInfo(AddDiseaseInformationRequest request, JwtUser jwtUser);

    DiseaseInfoResponse getActiveDisease(JwtUser jwtUser);

    StatusResult editDiseaseInfo(EditDiseaseInformationRequest request, JwtUser jwtUser);

    StatusResult approveDiseaseBySick(ApproveDiseaseRequest request, JwtUser jwtUser);

    List<DiseaseInfoResponse> getProcessedDiseases(JwtUser jwtUser);

    StatusResult approveDiseaseByDecanat(String diseaseId, JwtUser jwtUser);

    StatusResult refundDiseaseToStudent(String diseaseId, String refundCause);

    StatusResult rejectDisease(String diseaseId, String rejectCause, JwtUser jwtUser);
}
