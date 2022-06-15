package ru.example.service;

import ru.example.dto.response.GroupResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface GroupService {

    List<GroupResponse> getAllGroupsByProfileId(String profileId);

    List<GroupResponse> getAllGroupsByInstituteId(String instituteId);

    List<GroupResponse> getAllGroups();

    List<GroupResponse> getAllGroupsByInterestedGroupsId(List<String> interestedGroupsIdList);

    GroupResponse getGroupById(String groupId);
}
