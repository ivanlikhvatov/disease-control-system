package ru.example.service;

import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.StatusResult;
import ru.example.dto.response.UniversityInfo;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.decanatAdditionalInfo.DecanatAdditionalInfo;
import ru.example.security.jwt.JwtUser;

import java.util.List;

public interface DecanatService {
    List<DiseaseInfoResponse> getProcessedDiseasesByInsitute(JwtUser jwtUser);

    List<DiseaseInfoResponse> getActiveDiseasesByInstitute(JwtUser jwtUser);

    List<DiseaseInfoResponse> getAllDiseaseInformationByInstitute(JwtUser jwtUser);

    StatusResult approveDiseaseByDecanat(String diseaseId, JwtUser jwtUser);

    StatusResult refundDiseaseToStudent(String diseaseId, String refundCause);

    StatusResult rejectDisease(String diseaseId, String rejectCause, JwtUser jwtUser);

    DecanatAdditionalInfo buildDecanatAdditionalInfo(UserInfoDto userInfoDto);

    UniversityInfo buildUniversityInfo(UserInfoDto userInfoDto);
}
