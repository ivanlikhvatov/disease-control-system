package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.directionProfile.DirectionProfile;
import ru.example.dao.entity.group.Group;
import ru.example.dao.entity.institute.Institute;
import ru.example.dao.entity.instituteDirection.InstituteDirection;
import ru.example.dto.response.GroupResponse;
import ru.example.mapper.GroupResponseMapper;
import ru.example.repository.DirectionProfileRepository;
import ru.example.repository.GroupRepository;
import ru.example.service.GroupService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repository;
    private final GroupResponseMapper mapper;

    @Override
    public List<GroupResponse> getAllGroupsByProfileId(String profileId) {
        List<Group> groups = repository.findAllByDirectionProfileId(profileId);
        return mapper.map(groups);
    }

    @Override
    public List<GroupResponse> getAllGroups() {
        List<Group> groups = repository.findAll();
        return mapper.map(groups);
    }

    @Override
    public List<GroupResponse> getAllGroupsByInstituteId(String instituteId) {
        List<Group> groups = Optional.of(repository.findAll())
                .orElse(Collections.emptyList())
                .stream()
                .filter(group -> isNeedInstitute(instituteId, group))
                .collect(Collectors.toList());

        return mapper.map(groups);
    }

    private boolean isNeedInstitute(String instituteId, Group group) {
        return Optional.ofNullable(group)
                .map(Group::getDirectionProfile)
                .map(DirectionProfile::getInstituteDirection)
                .map(InstituteDirection::getInstitute)
                .map(Institute::getId)
                .filter(instituteId::equals)
                .isPresent();
    }

}
