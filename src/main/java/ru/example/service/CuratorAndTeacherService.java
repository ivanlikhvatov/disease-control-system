package ru.example.service;

import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.UniversityInfo;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.additionalInfo.CuratorAndTeacherAdditionalInfo;
import ru.example.security.jwt.JwtUser;

import java.util.List;

public interface CuratorAndTeacherService {
    List<DiseaseInfoResponse> getActiveDiseasesByInterestedGroups(JwtUser jwtUser);

    List<DiseaseInfoResponse> getAllDiseasesInformationByInterestedGroups(JwtUser jwtUser);

    UniversityInfo buildUniversityInfo(UserInfoDto userInfoDto);

    CuratorAndTeacherAdditionalInfo buildCuratorAndTeacherAdditionalInfo(UserInfoDto userInfoDto);
}
