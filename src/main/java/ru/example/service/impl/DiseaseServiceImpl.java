package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.disease.Disease;
import ru.example.dto.response.DiseaseResponse;
import ru.example.mapper.DiseaseResponseMapper;
import ru.example.repository.DiseaseRepository;
import ru.example.service.DiseaseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final DiseaseResponseMapper diseaseResponseMapper;

    @Override
    public List<DiseaseResponse> getDiseases() {
        List<Disease> diseases = diseaseRepository.findAll();
        return diseaseResponseMapper.map(diseases);
    }
}
