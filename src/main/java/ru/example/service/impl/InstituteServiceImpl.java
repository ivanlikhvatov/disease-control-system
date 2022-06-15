package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.institute.Institute;
import ru.example.dto.response.InstituteResponse;
import ru.example.mapper.InstituteResponseMapper;
import ru.example.repository.InstituteRepository;
import ru.example.service.InstituteService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstituteServiceImpl implements InstituteService {

    private final InstituteRepository repository;
    private final InstituteResponseMapper mapper;

    @Override
    public List<InstituteResponse> getAllInstitutes() {
        List<Institute> institutes = repository.findAll();
        return mapper.map(institutes);
    }
}
