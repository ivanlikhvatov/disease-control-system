package ru.example.service;

import ru.example.dto.request.disease.DiseaseInformationRequest;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.DiseaseResponse;
import ru.example.dto.response.StatusResult;
import ru.example.security.jwt.JwtUser;

import java.util.List;

public interface DiseaseService {
    List<DiseaseResponse> getDiseases();

    StatusResult addDiseaseInfo(DiseaseInformationRequest request, JwtUser jwtUser);

    DiseaseInfoResponse getNotClosedDisease(JwtUser jwtUser);
}
