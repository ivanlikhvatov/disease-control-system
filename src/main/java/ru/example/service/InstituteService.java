package ru.example.service;

import ru.example.dto.response.InstituteResponse;

import java.util.List;

public interface InstituteService {
    List<InstituteResponse> getAllInstitutes();
}
