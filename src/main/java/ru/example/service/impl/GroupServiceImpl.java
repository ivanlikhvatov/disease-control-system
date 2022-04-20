package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.group.Group;
import ru.example.dto.response.GroupResponse;
import ru.example.mapper.GroupResponseMapper;
import ru.example.repository.DirectionProfileRepository;
import ru.example.repository.GroupRepository;
import ru.example.service.GroupService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repository;
    private final GroupResponseMapper mapper;

    @Override
    public List<GroupResponse> getGroups(String profileId) {
        List<Group> groups = repository.findAllByDirectionProfileId(profileId);
        return mapper.map(groups);
    }
}
