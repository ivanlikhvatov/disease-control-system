package ru.example.service;

import ru.example.dto.response.DiseaseResponse;

import java.util.List;

public interface DiseaseService {
    List<DiseaseResponse> getDiseases();
}
