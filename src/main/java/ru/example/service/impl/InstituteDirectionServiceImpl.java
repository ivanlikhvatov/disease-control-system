package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.instituteDirection.InstituteDirection;
import ru.example.dto.response.InstituteDirectionResponse;
import ru.example.mapper.InstituteDirectionResponseMapper;
import ru.example.repository.InstituteDirectionRepository;
import ru.example.service.InstituteDirectionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstituteDirectionServiceImpl implements InstituteDirectionService {

    private final InstituteDirectionRepository repository;
    private final InstituteDirectionResponseMapper mapper;

    @Override
    public List<InstituteDirectionResponse> getInstituteDirections(String instituteId) {
        List<InstituteDirection> instituteDirections = repository.findAllByInstituteId(instituteId);
        return mapper.map(instituteDirections);
    }
}
