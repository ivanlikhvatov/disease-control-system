package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.department.Department;
import ru.example.dao.entity.instituteDirection.InstituteDirection;
import ru.example.dto.response.DepartmentResponse;
import ru.example.mapper.DepartmentResponseMapper;
import ru.example.repository.DepartmentRepository;
import ru.example.repository.InstituteDirectionRepository;
import ru.example.service.DepartmentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final InstituteDirectionRepository instituteDirectionRepository;
    private final DepartmentResponseMapper departmentResponseMapper;


    @Override
    public List<DepartmentResponse> getAllDepartmentsByInstituteId(String instituteId) {

        List<InstituteDirection> instituteDirections = instituteDirectionRepository.findAllByInstituteId(instituteId);

        List<Department> departments = Optional.ofNullable(instituteDirections)
                .orElse(Collections.emptyList())
                .stream()
                .map(InstituteDirection::getDepartment)
                .distinct()
                .collect(Collectors.toList());

        return departmentResponseMapper.map(departments);
    }
}
