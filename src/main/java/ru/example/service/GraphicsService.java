package ru.example.service;

import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dto.request.graphics.GroupGraphicRequest;
import ru.example.dto.response.graphics.CountOfDiseasesByDays;
import ru.example.dto.response.graphics.GroupGraphicInfo;
import ru.example.security.jwt.JwtUser;

import java.time.LocalDate;
import java.util.List;


public interface GraphicsService {
    GroupGraphicInfo getGroupGraphicInfo(GroupGraphicRequest groupGraphicRequest, JwtUser jwtUser);

    CountOfDiseasesByDays getCountOfDiseasesByDays(List<DiseaseInformation> diseases, LocalDate startDate, LocalDate endDate);
}
