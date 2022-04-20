package ru.example.service;

import ru.example.dto.response.DirectionProfileResponse;

import java.util.List;

public interface DirectionProfileService {
    List<DirectionProfileResponse> getDirectionProfiles(String directionId);
}
