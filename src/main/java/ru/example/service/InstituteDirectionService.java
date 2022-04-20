package ru.example.service;

import ru.example.dto.response.InstituteDirectionResponse;

import java.util.List;

public interface InstituteDirectionService {
    List<InstituteDirectionResponse> getInstituteDirections(String instituteId);
}
