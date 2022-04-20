package ru.example.service;

import ru.example.dto.response.GroupResponse;

import java.util.List;

public interface GroupService {

    List<GroupResponse> getGroups(String profileId);
}
