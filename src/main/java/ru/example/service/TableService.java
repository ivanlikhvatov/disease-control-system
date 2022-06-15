package ru.example.service;

import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.security.jwt.JwtUser;

import java.util.List;

public interface TableService {
    List<DiseaseInfoResponse> getActiveDiseases(JwtUser jwtUser);

    List<DiseaseInfoResponse> getAllDiseaseInformation(JwtUser jwtUser);
}
