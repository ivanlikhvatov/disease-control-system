package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dto.response.*;
import ru.example.dto.response.additionalInfo.RectoratAdditionalInfo;
import ru.example.dto.response.graphics.CountOfDiseasesByDays;
import ru.example.dto.response.graphics.DiseaseTypeCountOfSick;
import ru.example.dto.response.graphics.UniversityPartCountOfSick;
import ru.example.mapper.DiseaseInfoResponseMapper;
import ru.example.security.jwt.JwtUser;
import ru.example.service.*;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RectoratServiceImpl implements RectoratService {

    private final static int TWO_WEEKS_DAYS_COUNT = 14;
    private final static int ONE_DAY = 1;

    private final GroupService groupService;
    private final DepartmentService departmentService;
    private final InstituteService instituteService;

    private final DiseaseService diseaseService;
    private final GraphicsService graphicsService;
    private final DiseaseInfoResponseMapper diseaseInfoResponseMapper;

    @Override
    public List<DiseaseInfoResponse> getActiveDiseasesByUniversity(JwtUser jwtUser) {
        List<DiseaseInformation> activeDiseases = diseaseService
                .getAllDiseasesInStatus(DiseaseStatus.ACTIVE);

        return diseaseInfoResponseMapper.map(activeDiseases);
    }

    @Override
    public List<DiseaseInfoResponse> getAllDiseaseInformationByUniversity(JwtUser jwtUser) {
        List<DiseaseInformation> allDiseases = diseaseService
                .getAllDiseasesInformation();

        return diseaseService.buildDiseasesResponseWithScannedCertificate(allDiseases);
    }

    @Override
    public UniversityInfo buildUniversityInfo(UserInfoDto userInfoDto) {
        UniversityInfo universityInfo = new UniversityInfo();

        List<GroupResponse> groups = groupService.getAllGroups();
        List<DepartmentResponse> departments = departmentService.getAllDepartments();
        List<InstituteResponse> institutes = instituteService.getAllInstitutes();

        universityInfo.setDepartments(departments);
        universityInfo.setGroups(groups);
        universityInfo.setInstitutes(institutes);

        return universityInfo;
    }

    @Override
    public RectoratAdditionalInfo buildRectoratAdditionalInfo() {
        RectoratAdditionalInfo rectoratAdditionalInfo = new RectoratAdditionalInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = getCountOfDiseasesByDaysForTwoWeeks();
        String countOfSickNow = getCountOfSickNowInUniversity();
        String countOfRecoverToday = getCountOfRecoverTodayInUniversity();
        String countOfSickToday = getCountOfSickToday();
        List<UniversityPartCountOfSick> institutesCountOfSicks = graphicsService.buildInstituteCountOfSicksForRectorat();
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = getDiseasesByTypeCountOfSick();

        rectoratAdditionalInfo.setCountOfDiseasesByDaysForTwoWeeks(countOfDiseasesByDays);
        rectoratAdditionalInfo.setCountOfSickNow(countOfSickNow);
        rectoratAdditionalInfo.setUniversityPartCountOfSicks(institutesCountOfSicks);

        rectoratAdditionalInfo.setCountOfRecoverToday(countOfRecoverToday);
        rectoratAdditionalInfo.setCountOfSickToday(countOfSickToday);
        rectoratAdditionalInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);

        return rectoratAdditionalInfo;
    }

    private List<DiseaseTypeCountOfSick> getDiseasesByTypeCountOfSick() {
        List<DiseaseInformation> diseaseInformationList = diseaseService.getAllDiseasesInStatus(DiseaseStatus.ACTIVE);

        return graphicsService.buildCountOfDiseasesByType(diseaseInformationList);
    }

    private String getCountOfSickToday() {
        List<DiseaseInformation> sickTodayDiseasesByInstitute = diseaseService.getAllSickTodayDiseases();

        return String.valueOf(
                sickTodayDiseasesByInstitute.size()
        );
    }

    private String getCountOfRecoverTodayInUniversity() {
        List<DiseaseInformation> recoverTodayDiseases = diseaseService.getAllRecoverTodayDiseases();

        return String.valueOf(
                recoverTodayDiseases.size()
        );
    }

    private CountOfDiseasesByDays getCountOfDiseasesByDaysForTwoWeeks() {
        LocalDate startDate = LocalDate.now().minusDays(TWO_WEEKS_DAYS_COUNT - ONE_DAY);
        LocalDate endDate = LocalDate.now();

        List<DiseaseInformation> notRejectedDiseases = diseaseService.getAllNotRejectedDiseases();

        return graphicsService.getCountOfDiseasesByDays(notRejectedDiseases, startDate, endDate);
    }

    private String getCountOfSickNowInUniversity() {
        List<DiseaseInformation> diseaseInformationList = diseaseService.getAllDiseasesInStatus(DiseaseStatus.ACTIVE);

        return String.valueOf(
                diseaseInformationList.size()
        );
    }
}
