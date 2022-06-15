package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.user.User;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.GroupResponse;
import ru.example.dto.response.UniversityInfo;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.additionalInfo.CuratorAndTeacherAdditionalInfo;
import ru.example.dto.response.graphics.CountOfDiseasesByDays;
import ru.example.dto.response.graphics.DiseaseTypeCountOfSick;
import ru.example.dto.response.graphics.UniversityPartCountOfSick;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.DiseaseInfoResponseMapper;
import ru.example.repository.UserRepository;
import ru.example.security.jwt.JwtUser;
import ru.example.service.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CuratorAndTeacherServiceImpl implements CuratorAndTeacherService {
    private final static int TWO_WEEKS_DAYS_COUNT = 14;
    private final static int ONE_DAY = 1;

    private final DiseaseInfoResponseMapper diseaseInfoResponseMapper;
    private final DiseaseService diseaseService;

    private final UserRepository userRepository;
    private final GroupService groupService;
    private final DepartmentService departmentService;
    private final GraphicsService graphicsService;

    @Override
    public List<DiseaseInfoResponse> getActiveDiseasesByInterestedGroups(JwtUser jwtUser) {
        User user = getUserByLogin(jwtUser.getLogin());
        List<DiseaseInformation> diseasesFromNeedGroups = buildDiseaseInStatusListByNeedGroups(user.getInterestedGroupsIdList(), DiseaseStatus.ACTIVE);

        return diseaseInfoResponseMapper.map(diseasesFromNeedGroups);
    }

    @Override
    public List<DiseaseInfoResponse> getAllDiseasesInformationByInterestedGroups(JwtUser jwtUser) {
        User user = getUserByLogin(jwtUser.getLogin());
        List<DiseaseInformation> diseasesFromNeedGroups = buildAllDiseaseListByNeedGroups(user.getInterestedGroupsIdList());

        return diseaseInfoResponseMapper.map(diseasesFromNeedGroups);
    }

    @Override
    public UniversityInfo buildUniversityInfo(UserInfoDto userInfoDto) {
        UniversityInfo universityInfo = new UniversityInfo();
        List<GroupResponse> groups = groupService.getAllGroupsByInterestedGroupsId(userInfoDto.getInterestedGroupsIdList());

        universityInfo.setGroups(groups);

        return universityInfo;
    }

    @Override
    public CuratorAndTeacherAdditionalInfo buildCuratorAndTeacherAdditionalInfo(UserInfoDto userInfoDto) {
        CuratorAndTeacherAdditionalInfo curatorAndTeacherAdditionalInfo = new CuratorAndTeacherAdditionalInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = getCountOfDiseasesByDaysForTwoWeeks(userInfoDto);
        String countOfSickNow = getCountOfSickNowInInterestedGroups(userInfoDto);
        String countOfRecoverToday = getCountOfRecoverTodayInGroups(userInfoDto);
        String countOfSickToday = getCountOfSickTodayInGroups(userInfoDto);
        List<UniversityPartCountOfSick> groupsCountOfSicks = graphicsService.buildGroupsCountOfSicksForCuratorAndTeacher(userInfoDto);
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = getDiseasesByTypeCountOfSickInGroup(userInfoDto);

        curatorAndTeacherAdditionalInfo.setCountOfDiseasesByDaysForTwoWeeks(countOfDiseasesByDays);
        curatorAndTeacherAdditionalInfo.setCountOfSickNow(countOfSickNow);
        curatorAndTeacherAdditionalInfo.setUniversityPartCountOfSicks(groupsCountOfSicks);
        curatorAndTeacherAdditionalInfo.setCountOfRecoverToday(countOfRecoverToday);
        curatorAndTeacherAdditionalInfo.setCountOfSickToday(countOfSickToday);
        curatorAndTeacherAdditionalInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);

        return curatorAndTeacherAdditionalInfo;
    }

    private List<DiseaseTypeCountOfSick> getDiseasesByTypeCountOfSickInGroup(UserInfoDto userInfoDto) {
        List<DiseaseInformation> diseaseInformationList = buildDiseaseInStatusListByNeedGroups(userInfoDto.getInterestedGroupsIdList(), DiseaseStatus.ACTIVE);

        return graphicsService.buildCountOfDiseasesByType(diseaseInformationList);
    }

    private String getCountOfSickTodayInGroups(UserInfoDto userInfoDto) {

        List<DiseaseInformation> sickTodayDiseasesByInstitute = Optional.ofNullable(userInfoDto.getInterestedGroupsIdList())
                .orElse(Collections.emptyList())
                .stream()
                .flatMap(groupId -> diseaseService.getSickTodayDiseasesByGroup(groupId).stream())
                .collect(Collectors.toList());

        return String.valueOf(
                sickTodayDiseasesByInstitute.size()
        );
    }

    private String getCountOfRecoverTodayInGroups(UserInfoDto userInfoDto) {
        List<DiseaseInformation> recoverTodayDiseasesByInstitute = Optional.ofNullable(userInfoDto.getInterestedGroupsIdList())
                .orElse(Collections.emptyList())
                .stream()
                .flatMap(groupId -> diseaseService.getRecoverTodayByGroup(groupId).stream())
                .collect(Collectors.toList());

        return String.valueOf(
                recoverTodayDiseasesByInstitute.size()
        );
    }

    private String getCountOfSickNowInInterestedGroups(UserInfoDto userInfoDto) {

        List<DiseaseInformation> diseaseInformationList = buildDiseaseInStatusListByNeedGroups(userInfoDto.getInterestedGroupsIdList(), DiseaseStatus.ACTIVE);

        return String.valueOf(
                diseaseInformationList.size()
        );
    }

    private CountOfDiseasesByDays getCountOfDiseasesByDaysForTwoWeeks(UserInfoDto userInfoDto) {
        LocalDate startDate = LocalDate.now().minusDays(TWO_WEEKS_DAYS_COUNT - ONE_DAY);
        LocalDate endDate = LocalDate.now();

        List<DiseaseInformation> notRejectedDiseases = buildNotRejectedDiseasesListByNeedGroups(userInfoDto.getInterestedGroupsIdList());

        return graphicsService.getCountOfDiseasesByDays(notRejectedDiseases, startDate, endDate);
    }

    private List<DiseaseInformation> buildAllDiseaseListByNeedGroups(List<String> interestedGroupsIdList) {
        return Optional.ofNullable(interestedGroupsIdList)
                .orElse(Collections.emptyList())
                .stream()
                .flatMap(groupId -> diseaseService.getAllDiseasesByGroup(groupId).stream())
                .collect(Collectors.toList());
    }

    private List<DiseaseInformation> buildDiseaseInStatusListByNeedGroups(List<String> interestedGroupsIdList, DiseaseStatus status) {
        return Optional.ofNullable(interestedGroupsIdList)
                .orElse(Collections.emptyList())
                .stream()
                .flatMap(groupId -> diseaseService.getDiseasesInStatusByGroup(status, groupId).stream())
                .collect(Collectors.toList());
    }

    private List<DiseaseInformation> buildNotRejectedDiseasesListByNeedGroups(List<String> interestedGroupsIdList) {
        return Optional.ofNullable(interestedGroupsIdList)
                .orElse(Collections.emptyList())
                .stream()
                .flatMap(groupId -> diseaseService.getNotRejectedDiseasesByGroup(groupId).stream())
                .collect(Collectors.toList());
    }

    public User getUserByLogin(String login) {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new ApiException(ErrorContainer.USER_NOT_FOUND);
        }

        return user;
    }

    public User getUserById(String id) {
        return Optional.of(userRepository.findById(id))
                .get()
                .orElse(null);
    }
}
