package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.directionProfile.DirectionProfile;
import ru.example.dto.response.DirectionProfileResponse;
import ru.example.repository.DirectionProfileRepository;
import ru.example.service.DirectionProfileService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectionProfileServiceImpl implements DirectionProfileService {

    private final DirectionProfileRepository repository;

    @Override
    public List<DirectionProfileResponse> getDirectionProfiles(String directionId) {
        List<DirectionProfile> directionProfiles = repository.findAllByInstituteDirectionId(directionId);
        return null;
    }
}
