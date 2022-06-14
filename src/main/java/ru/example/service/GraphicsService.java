package ru.example.service;

import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dto.request.graphics.DepartmentGraphicRequest;
import ru.example.dto.request.graphics.GroupGraphicRequest;
import ru.example.dto.request.graphics.InstituteGraphicRequest;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.graphics.*;
import ru.example.security.jwt.JwtUser;

import java.time.LocalDate;
import java.util.List;


public interface GraphicsService {
    GroupGraphicInfo getGroupGraphicInfo(GroupGraphicRequest groupGraphicRequest, JwtUser jwtUser);

    CountOfDiseasesByDays getCountOfDiseasesByDays(List<DiseaseInformation> diseases, LocalDate startDate, LocalDate endDate);

    List<DiseaseTypeCountOfSick> buildCountOfDiseasesByType(List<DiseaseInformation> diseaseInformationList);

    DepartmentGraphicInfo getDepartmentGraphicInfo(DepartmentGraphicRequest departmentGraphicRequest, JwtUser jwtUser);

    List<UniversityPartCountOfSick> buildDepartmentCountOfSicksForDecanat(UserInfoDto userInfoDto);

    InstituteGraphicInfo getInstituteGraphicInfo(InstituteGraphicRequest instituteGraphicRequest, JwtUser jwtUser);
}
