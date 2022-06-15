package ru.example.service;

import ru.example.dto.response.GroupResponse;

import java.util.List;

public interface GroupService {

    List<GroupResponse> getAllGroupsByProfileId(String profileId);

    List<GroupResponse> getAllGroupsByInstituteId(String instituteId);

    List<GroupResponse> getAllGroups();
}
