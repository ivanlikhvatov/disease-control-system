package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.directionProfile.DirectionProfile;
import ru.example.dto.response.DirectionProfileResponse;

import java.util.List;

@Mapper
public interface DirectionProfileResponseMapper {
    DirectionProfileResponse map(DirectionProfile directionProfile);

    List<DirectionProfileResponse> map(List<DirectionProfile> directionProfiles);
}
