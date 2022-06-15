package ru.example.service;

import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.UniversityInfo;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.additionalInfo.RectoratAdditionalInfo;
import ru.example.security.jwt.JwtUser;

import java.util.List;

public interface RectoratService {
    List<DiseaseInfoResponse> getActiveDiseasesByUniversity(JwtUser jwtUser);

    List<DiseaseInfoResponse> getAllDiseaseInformationByUniversity(JwtUser jwtUser);

    UniversityInfo buildUniversityInfo(UserInfoDto userInfoDto);

    RectoratAdditionalInfo buildRectoratAdditionalInfo();
}
