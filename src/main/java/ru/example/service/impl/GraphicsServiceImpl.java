package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.disease.Disease;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.user.Role;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.creationService.DepartmentRequest;
import ru.example.dto.request.creationService.GroupRequest;
import ru.example.dto.request.creationService.InstituteRequest;
import ru.example.dto.request.graphics.DepartmentGraphicRequest;
import ru.example.dto.request.graphics.GroupGraphicRequest;
import ru.example.dto.request.graphics.InstituteGraphicRequest;
import ru.example.dto.request.graphics.UniversityGraphicRequest;
import ru.example.dto.response.DepartmentResponse;
import ru.example.dto.response.GroupResponse;
import ru.example.dto.response.InstituteResponse;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.graphics.*;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.UserInfoResponseDtoMapper;
import ru.example.repository.UserRepository;
import ru.example.security.jwt.JwtUser;
import ru.example.service.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GraphicsServiceImpl implements GraphicsService {

    private final static int ONE_DAY = 1;
    private final static String OTHER_TYPE_DISEASE_NAME = "Другое";
    private final static DateTimeFormatter DAY_AND_MONTH_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM");

    private final UserRepository userRepository;
    private final DiseaseService diseaseService;
    private final GroupService groupService;
    private final DepartmentService departmentService;
    private final InstituteService instituteService;

    private final UserInfoResponseDtoMapper userInfoResponseDtoMapper;

    @Override
    public UniversityGraphicInfo getUniversityGraphicInfo(UniversityGraphicRequest universityGraphicRequest, JwtUser jwtUser) {
        UniversityGraphicInfo universityGraphicInfo = new UniversityGraphicInfo();

        if (jwtUser.getRoles().contains(Role.RECTORAT)) {
            CountOfDiseasesByDays countOfDiseasesByDays = buildCountOfDiseasesByDaysInUniversity(universityGraphicRequest);
            List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = buildCountOfDiseasesByTypeInUniversity();

            universityGraphicInfo.setCountOfDiseasesByDays(countOfDiseasesByDays);
            universityGraphicInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);
        }

        return universityGraphicInfo;
    }

    @Override
    public GroupGraphicInfo getGroupGraphicInfo(GroupGraphicRequest groupGraphicRequest, JwtUser jwtUser) {
        User user = getUserByLogin(jwtUser.getLogin());
        UserInfoDto userInfoDto = userInfoResponseDtoMapper.map(user);

        if (user.getRoles().contains(Role.DECANAT)) {
            return buildDecanatGroupGraphicInfo(userInfoDto, groupGraphicRequest);
        }

        if (user.getRoles().contains(Role.RECTORAT)) {
            return buildRectoratGroupGraphicInfo(userInfoDto, groupGraphicRequest);
        }

        return new GroupGraphicInfo();
    }

    @Override
    public DepartmentGraphicInfo getDepartmentGraphicInfo(DepartmentGraphicRequest departmentGraphicRequest, JwtUser jwtUser) {
        User user = getUserByLogin(jwtUser.getLogin());
        UserInfoDto userInfoDto = userInfoResponseDtoMapper.map(user);

        if (user.getRoles().contains(Role.DECANAT)) {
            return buildDecanatDepartmentGraphicInfo(userInfoDto, departmentGraphicRequest);
        }

        if (user.getRoles().contains(Role.RECTORAT)) {
            return buildRectoratDepartmentGraphicInfo(userInfoDto, departmentGraphicRequest);
        }

        return new DepartmentGraphicInfo();
    }

    @Override
    public InstituteGraphicInfo getInstituteGraphicInfo(InstituteGraphicRequest instituteGraphicRequest, JwtUser jwtUser) {
        User user = getUserByLogin(jwtUser.getLogin());
        UserInfoDto userInfoDto = userInfoResponseDtoMapper.map(user);

        if (user.getRoles().contains(Role.DECANAT)) {
            return buildDecanatInstituteGraphicInfo(userInfoDto, instituteGraphicRequest);
        }

        if (user.getRoles().contains(Role.RECTORAT)) {
            return buildRectoratInstituteGraphicInfo(userInfoDto, instituteGraphicRequest);
        }

        return new InstituteGraphicInfo();
    }

    private InstituteGraphicInfo buildDecanatInstituteGraphicInfo(UserInfoDto userInfoDto, InstituteGraphicRequest instituteGraphicRequest) {
        InstituteGraphicInfo instituteGraphicInfo = new InstituteGraphicInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = buildCountOfDiseasesByDaysInInstitute(instituteGraphicRequest);
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = buildCountOfDiseasesByTypeInInstitute(instituteGraphicRequest);

        instituteGraphicInfo.setCountOfDiseasesByDays(countOfDiseasesByDays);
        instituteGraphicInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);

        return instituteGraphicInfo;
    }

    private InstituteGraphicInfo buildRectoratInstituteGraphicInfo(UserInfoDto userInfoDto, InstituteGraphicRequest instituteGraphicRequest) {
        InstituteGraphicInfo instituteGraphicInfo = new InstituteGraphicInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = buildCountOfDiseasesByDaysInInstitute(instituteGraphicRequest);
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = buildCountOfDiseasesByTypeInInstitute(instituteGraphicRequest);
        List<UniversityPartCountOfSick> instituteCountOfSicks = buildInstituteCountOfSicksForRectorat();


        instituteGraphicInfo.setCountOfDiseasesByDays(countOfDiseasesByDays);
        instituteGraphicInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);
        instituteGraphicInfo.setUniversityPartCountOfSicks(instituteCountOfSicks);

        return instituteGraphicInfo;
    }

    private DepartmentGraphicInfo buildDecanatDepartmentGraphicInfo(UserInfoDto userInfoDto, DepartmentGraphicRequest departmentGraphicRequest) {
        DepartmentGraphicInfo departmentGraphicInfo = new DepartmentGraphicInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = buildCountOfDiseasesByDaysInDepartment(departmentGraphicRequest);
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = buildCountOfDiseasesByTypeInDepartment(departmentGraphicRequest);
        List<UniversityPartCountOfSick> departmentsCountOfSicks = buildDepartmentCountOfSicksForDecanat(userInfoDto);

        departmentGraphicInfo.setCountOfDiseasesByDays(countOfDiseasesByDays);
        departmentGraphicInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);
        departmentGraphicInfo.setUniversityPartCountOfSicks(departmentsCountOfSicks);

        return departmentGraphicInfo;
    }

    private DepartmentGraphicInfo buildRectoratDepartmentGraphicInfo(UserInfoDto userInfoDto, DepartmentGraphicRequest departmentGraphicRequest) {
        DepartmentGraphicInfo departmentGraphicInfo = new DepartmentGraphicInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = buildCountOfDiseasesByDaysInDepartment(departmentGraphicRequest);
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = buildCountOfDiseasesByTypeInDepartment(departmentGraphicRequest);
        List<UniversityPartCountOfSick> departmentsCountOfSicks = buildDepartmentCountOfSicksForRectorat();

        departmentGraphicInfo.setCountOfDiseasesByDays(countOfDiseasesByDays);
        departmentGraphicInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);
        departmentGraphicInfo.setUniversityPartCountOfSicks(departmentsCountOfSicks);

        return departmentGraphicInfo;
    }

    private CountOfDiseasesByDays buildCountOfDiseasesByDaysInDepartment(DepartmentGraphicRequest departmentGraphicRequest) {
        String departmentId = getDepartmentIdFromDepartmentGraphicRequest(departmentGraphicRequest);
        LocalDate startDate = departmentGraphicRequest.getStartDate();
        LocalDate endDate = departmentGraphicRequest.getEndDate();

        List<DiseaseInformation> diseaseInformationByGroup = diseaseService.getNotRejectedDiseasesByDepartment(departmentId);

        return getCountOfDiseasesByDays(diseaseInformationByGroup, startDate, endDate);
    }

    private CountOfDiseasesByDays buildCountOfDiseasesByDaysInInstitute(InstituteGraphicRequest instituteGraphicRequest) {
        String instituteId = getInstituteIdFromInstituteGraphicRequest(instituteGraphicRequest);
        LocalDate startDate = instituteGraphicRequest.getStartDate();
        LocalDate endDate = instituteGraphicRequest.getEndDate();

        List<DiseaseInformation> diseaseInformationByGroup = diseaseService.getNotRejectedDiseasesByInstitute(instituteId);

        return getCountOfDiseasesByDays(diseaseInformationByGroup, startDate, endDate);
    }

    private CountOfDiseasesByDays buildCountOfDiseasesByDaysInUniversity(UniversityGraphicRequest universityGraphicRequest) {
        LocalDate startDate = universityGraphicRequest.getStartDate();
        LocalDate endDate = universityGraphicRequest.getEndDate();

        List<DiseaseInformation> diseaseInformationByGroup = diseaseService.getAllNotRejectedDiseases();

        return getCountOfDiseasesByDays(diseaseInformationByGroup, startDate, endDate);
    }

    private GroupGraphicInfo buildDecanatGroupGraphicInfo(UserInfoDto userInfoDto, GroupGraphicRequest groupGraphicRequest) {

        GroupGraphicInfo groupGraphicInfo = new GroupGraphicInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = buildCountOfDiseasesByDaysInGroup(groupGraphicRequest);
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = buildCountOfDiseasesByTypeInGroup(groupGraphicRequest);
        List<UniversityPartCountOfSick> groupsCountOfSicks = buildGroupsCountOfSicksForDecanat(userInfoDto);

        groupGraphicInfo.setCountOfDiseasesByDays(countOfDiseasesByDays);
        groupGraphicInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);
        groupGraphicInfo.setUniversityPartCountOfSicks(groupsCountOfSicks);

        return groupGraphicInfo;
    }

    private GroupGraphicInfo buildRectoratGroupGraphicInfo(UserInfoDto userInfoDto, GroupGraphicRequest groupGraphicRequest) {
        GroupGraphicInfo groupGraphicInfo = new GroupGraphicInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = buildCountOfDiseasesByDaysInGroup(groupGraphicRequest);
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = buildCountOfDiseasesByTypeInGroup(groupGraphicRequest);
        List<UniversityPartCountOfSick> groupsCountOfSicks = buildGroupsCountOfSicksForRectorat();

        groupGraphicInfo.setCountOfDiseasesByDays(countOfDiseasesByDays);
        groupGraphicInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);
        groupGraphicInfo.setUniversityPartCountOfSicks(groupsCountOfSicks);

        return groupGraphicInfo;
    }

    @Override
    public List<UniversityPartCountOfSick> buildDepartmentCountOfSicksForDecanat(UserInfoDto userInfoDto) {
        String instituteId = getDecanatInstituteId(userInfoDto);
        List<DepartmentResponse> departments = departmentService.getAllDepartmentsByInstituteId(instituteId);

        return getDepartmentCountOfSicks(departments);
    }

    private List<UniversityPartCountOfSick> buildDepartmentCountOfSicksForRectorat() {
        List<DepartmentResponse> departments = departmentService.getAllDepartments();

        return getDepartmentCountOfSicks(departments);
    }

    @Override
    public List<UniversityPartCountOfSick> buildInstituteCountOfSicksForRectorat() {
        List<InstituteResponse> institutes = instituteService.getAllInstitutes();

        return getInstituteCountOfSicks(institutes);
    }

    private List<UniversityPartCountOfSick> buildGroupsCountOfSicksForDecanat(UserInfoDto userInfoDto) {
        String instituteId = getDecanatInstituteId(userInfoDto);
        List<GroupResponse> groups = groupService.getAllGroupsByInstituteId(instituteId);

        return getGroupsCountOfSicks(groups);
    }

    private List<UniversityPartCountOfSick> buildGroupsCountOfSicksForRectorat() {
        List<GroupResponse> groups = groupService.getAllGroups();

        return getGroupsCountOfSicks(groups);
    }

    private List<UniversityPartCountOfSick> getDepartmentCountOfSicks(List<DepartmentResponse> departments) {
        return Optional.ofNullable(departments)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::buildDepartmentCountOfSick)
                .collect(Collectors.toList());
    }

    private List<UniversityPartCountOfSick> getInstituteCountOfSicks(List<InstituteResponse> institutes) {
        return Optional.ofNullable(institutes)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::buildInstituteCountOfSick)
                .collect(Collectors.toList());
    }

    private List<UniversityPartCountOfSick> getGroupsCountOfSicks(List<GroupResponse> groups) {

        return Optional.ofNullable(groups)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::buildGroupCountOfSick)
                .collect(Collectors.toList());
    }


    private UniversityPartCountOfSick buildDepartmentCountOfSick(DepartmentResponse department) {
        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByDepartment(DiseaseStatus.ACTIVE, department.getId());

        UniversityPartCountOfSick departmentCountOfSick = new UniversityPartCountOfSick();
        departmentCountOfSick.setName(department.getShortName());
        departmentCountOfSick.setCountOfSick(diseaseInformationList.size());

        return departmentCountOfSick;
    }

    private UniversityPartCountOfSick buildInstituteCountOfSick(InstituteResponse institute) {
        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByInstitute(DiseaseStatus.ACTIVE, institute.getId());

        UniversityPartCountOfSick departmentCountOfSick = new UniversityPartCountOfSick();
        departmentCountOfSick.setName(institute.getShortName());
        departmentCountOfSick.setCountOfSick(diseaseInformationList.size());

        return departmentCountOfSick;
    }

    private UniversityPartCountOfSick buildGroupCountOfSick(GroupResponse group) {
        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByGroup(DiseaseStatus.ACTIVE, group.getId());

        UniversityPartCountOfSick groupCountOfSick = new UniversityPartCountOfSick();
        groupCountOfSick.setName(group.getName());
        groupCountOfSick.setCountOfSick(diseaseInformationList.size());

        return groupCountOfSick;
    }

    private List<DiseaseTypeCountOfSick> buildCountOfDiseasesByTypeInGroup(GroupGraphicRequest groupGraphicRequest) {
        String groupId = getGroupIdFromGroupGraphicRequest(groupGraphicRequest);
        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByGroup(DiseaseStatus.ACTIVE, groupId);

        return buildCountOfDiseasesByType(diseaseInformationList);
    }

    private List<DiseaseTypeCountOfSick> buildCountOfDiseasesByTypeInDepartment(DepartmentGraphicRequest departmentGraphicRequest) {
        String departmentId = getDepartmentIdFromDepartmentGraphicRequest(departmentGraphicRequest);
        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByDepartment(DiseaseStatus.ACTIVE, departmentId);

        return buildCountOfDiseasesByType(diseaseInformationList);
    }

    private List<DiseaseTypeCountOfSick> buildCountOfDiseasesByTypeInUniversity() {
        List<DiseaseInformation> diseaseInformationList = diseaseService.getAllDiseasesInStatus(DiseaseStatus.ACTIVE);

        return buildCountOfDiseasesByType(diseaseInformationList);
    }

    private List<DiseaseTypeCountOfSick> buildCountOfDiseasesByTypeInInstitute(InstituteGraphicRequest instituteGraphicRequest) {
        String instituteId = getInstituteIdFromInstituteGraphicRequest(instituteGraphicRequest);
        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByInstitute(DiseaseStatus.ACTIVE, instituteId);

        return buildCountOfDiseasesByType(diseaseInformationList);
    }

    @Override
    public List<DiseaseTypeCountOfSick> buildCountOfDiseasesByType(List<DiseaseInformation> diseaseInformationList) {
        List<Disease> diseases = diseaseService.getDiseases();

        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = Optional.ofNullable(diseases)
                .orElse(Collections.emptyList())
                .stream()
                .map(disease -> buildDiseaseTypeCountOfSick(disease, diseaseInformationList))
                .collect(Collectors.toList());

        DiseaseTypeCountOfSick otherDiseaseTypeCountOfSick = buildOtherDiseaseTypeCountOfSick(diseaseInformationList);
        diseaseTypeCountOfSicks.add(otherDiseaseTypeCountOfSick);

        return diseaseTypeCountOfSicks;
    }


    private DiseaseTypeCountOfSick buildOtherDiseaseTypeCountOfSick(List<DiseaseInformation> diseaseInformationList) {
        DiseaseTypeCountOfSick diseaseTypeCountOfSick = new DiseaseTypeCountOfSick();

        long countOfSick = Optional.ofNullable(diseaseInformationList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(diseaseInformation -> StringUtils.isNotBlank(diseaseInformation.getOtherDiseaseInformation()))
                .count();

        diseaseTypeCountOfSick.setCountOfSick(countOfSick);
        diseaseTypeCountOfSick.setDiseaseName(OTHER_TYPE_DISEASE_NAME);

        return diseaseTypeCountOfSick;
    }

    private DiseaseTypeCountOfSick buildDiseaseTypeCountOfSick(Disease disease, List<DiseaseInformation> diseaseInformationList) {
        String diseaseId = disease.getId();
        DiseaseTypeCountOfSick diseaseTypeCountOfSick = new DiseaseTypeCountOfSick();

        long countOfSick = Optional.ofNullable(diseaseInformationList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(diseaseInformation -> diseaseInformation.getDisease() != null
                        && diseaseId.equals(diseaseInformation.getDisease().getId()))
                .count();

        diseaseTypeCountOfSick.setCountOfSick(countOfSick);
        diseaseTypeCountOfSick.setDiseaseName(disease.getName());

        return diseaseTypeCountOfSick;
    }




    private CountOfDiseasesByDays buildCountOfDiseasesByDaysInGroup(GroupGraphicRequest groupGraphicRequest) {
        String groupId = getGroupIdFromGroupGraphicRequest(groupGraphicRequest);
        LocalDate startDate = groupGraphicRequest.getStartDate();
        LocalDate endDate = groupGraphicRequest.getEndDate();

        List<DiseaseInformation> diseaseInformationByGroup = diseaseService.getNotRejectedDiseasesByGroup(groupId);

        return getCountOfDiseasesByDays(diseaseInformationByGroup, startDate, endDate);
    }

    @Override
    public CountOfDiseasesByDays getCountOfDiseasesByDays(List<DiseaseInformation> diseases, LocalDate startDate, LocalDate endDate) {

        endDate = endDate.plusDays(ONE_DAY);

        List<Long> countOfDiseasesByDates = getCountOfDiseasesByPeriod(diseases, startDate, endDate);
        List<String> datesInDatesRange = getDatesInTwoLatestWeek(startDate, endDate);


        CountOfDiseasesByDays countOfDiseasesByDays = new CountOfDiseasesByDays();
        countOfDiseasesByDays.setCountsOfSick(countOfDiseasesByDates);
        countOfDiseasesByDays.setDates(datesInDatesRange);

        return countOfDiseasesByDays;
    }


    private List<Long> getCountOfDiseasesByPeriod(List<DiseaseInformation> diseaseInformationList, LocalDate startDate, LocalDate endDate) {
        List<Long> countOfDiseasesByTwoWeeks = new ArrayList<>();

        List<LocalDate> dates = startDate.datesUntil(endDate).collect(Collectors.toList());

        dates
                .forEach(date -> {
                    Long countOfSick = calculateCountOfSickInDay(date, diseaseInformationList);
                    countOfDiseasesByTwoWeeks.add(countOfSick);
                });

        return countOfDiseasesByTwoWeeks;
    }

    private Long calculateCountOfSickInDay(LocalDate date, List<DiseaseInformation> diseaseInformationByTwoWeeks) {
        return Optional.ofNullable(diseaseInformationByTwoWeeks)
                .orElse(Collections.emptyList())
                .stream()
                .filter(diseaseInformation -> {
                    LocalDate dateDisease = diseaseInformation.getDateOfDisease();
                    LocalDate dateRecovery = diseaseInformation.getDateOfRecovery();
                    return (date.isEqual(dateDisease) || date.isAfter(dateDisease)) && (dateRecovery == null || date.isEqual(dateRecovery) || date.isBefore(dateRecovery));
                })
                .count();
    }

    private List<String> getDatesInTwoLatestWeek(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate)
                .map(DAY_AND_MONTH_DATE_FORMAT::format)
                .collect(Collectors.toList());
    }

    private String getGroupIdFromGroupGraphicRequest(GroupGraphicRequest groupGraphicRequest) {
        return Optional.ofNullable(groupGraphicRequest)
                .map(GroupGraphicRequest::getGroup)
                .map(GroupRequest::getId)
                .orElse(StringUtils.EMPTY);
    }

    private String getDepartmentIdFromDepartmentGraphicRequest(DepartmentGraphicRequest departmentGraphicRequest) {
        return Optional.ofNullable(departmentGraphicRequest)
                .map(DepartmentGraphicRequest::getDepartment)
                .map(DepartmentRequest::getId)
                .orElse(StringUtils.EMPTY);
    }

    private String getInstituteIdFromInstituteGraphicRequest(InstituteGraphicRequest instituteGraphicRequest) {
        return Optional.ofNullable(instituteGraphicRequest)
                .map(InstituteGraphicRequest::getInstitute)
                .map(InstituteRequest::getId)
                .orElse(StringUtils.EMPTY);
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

    private String getDecanatInstituteId(UserInfoDto decanatUser) {
        return Optional.ofNullable(decanatUser)
                .map(UserInfoDto::getInstitute)
                .map(InstituteResponse::getId)
                .orElse(StringUtils.EMPTY);
    }
}
