package ru.example.service;

import ru.example.dto.request.disease.AddDiseaseInformationRequest;
import ru.example.dto.request.disease.ApproveDiseaseRequest;
import ru.example.dto.request.disease.EditDiseaseInformationRequest;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.StatusResult;
import ru.example.security.jwt.JwtUser;

public interface StudentService {

    DiseaseInfoResponse getActiveDisease(JwtUser jwtUser);

    StatusResult addDiseaseInfo(AddDiseaseInformationRequest request, JwtUser jwtUser);

    StatusResult editDiseaseInfo(EditDiseaseInformationRequest request, JwtUser jwtUser);

    StatusResult approveDiseaseBySick(ApproveDiseaseRequest request, JwtUser jwtUser);
}
