package ru.example.service;

import ru.example.dto.response.DepartmentResponse;
import ru.example.dto.response.InstituteResponse;

import java.util.List;

public interface DepartmentService {
    List<DepartmentResponse> getAllDepartmentsByInstituteId(String instituteId);
}
