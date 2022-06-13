package ru.example.service;

import ru.example.dto.response.GroupResponse;

import java.util.List;

public interface GroupService {

    List<GroupResponse> getAllGroups(String profileId);

    List<GroupResponse> getAllGroupsByInstituteId(String instituteId);
}
