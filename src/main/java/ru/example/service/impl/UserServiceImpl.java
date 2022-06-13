package ru.example.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.department.Department;
import ru.example.dao.entity.disease.Disease;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.instituteDirection.InstituteDirection;
import ru.example.dao.entity.user.Role;
import ru.example.dao.entity.user.User;
import ru.example.dto.response.InstituteResponse;
import ru.example.dto.response.UserAdditionalInfo;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.decanatAdditionalInfo.CountOfDiseasesByDays;
import ru.example.dto.response.decanatAdditionalInfo.DecanatAdditionalInfo;
import ru.example.dto.response.decanatAdditionalInfo.DepartmentCountOfSick;
import ru.example.dto.response.decanatAdditionalInfo.DiseaseTypeCountOfSick;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.UserInfoResponseDtoMapper;
import ru.example.repository.DepartmentRepository;
import ru.example.repository.InstituteDirectionRepository;
import ru.example.repository.UserRepository;
import ru.example.service.DiseaseService;
import ru.example.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final static int TWO_WEEKS_DAYS_COUNT = 14;
    private final static int ZERO = 0;
    private final static int ONE_DAY = 1;
    private final static DateTimeFormatter DAY_AND_MONTH_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM");
    private final static String OTHER_TYPE_DISEASE_NAME = "Другое";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserInfoResponseDtoMapper mapper;
    private final DepartmentRepository departmentRepository;
    private final InstituteDirectionRepository instituteDirectionRepository;

    private DiseaseService diseaseService;

    @Autowired
    public void setDiseaseService(DiseaseService diseaseService) {
        this.diseaseService = diseaseService;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserInfoDto getUserInfoDtoByLogin(String login) {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new ApiException(ErrorContainer.USER_NOT_FOUND);
        }

        UserInfoDto userInfoDto = mapper.map(user);
        UserAdditionalInfo userAdditionalInfo = getUserAdditionalInfo(userInfoDto);

        userInfoDto.setAdditionalInfo(userAdditionalInfo);


        return userInfoDto;
    }

    @Override
    public UserAdditionalInfo getUserAdditionalInfo(UserInfoDto userInfoDto) {

        UserAdditionalInfo userAdditionalInfo = new UserAdditionalInfo();

        if (userInfoDto.getRoles().contains(Role.DECANAT)) {
            DecanatAdditionalInfo decanatAdditionalInfo = buildDecanatAdditionalInfo(userInfoDto);
            userAdditionalInfo.setDecanatAdditionalInfo(decanatAdditionalInfo);
        }

        return userAdditionalInfo;
    }

    private DecanatAdditionalInfo buildDecanatAdditionalInfo(UserInfoDto userInfoDto) {
        DecanatAdditionalInfo decanatAdditionalInfo = new DecanatAdditionalInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = getCountOfDiseasesByDaysForTwoWeeks(userInfoDto);
        String countOfSickNow = getCountOfSickNowInInstitute(userInfoDto);
        String countOfRecoverToday = getCountOfRecoverTodayInInstitute(userInfoDto);
        String countOfSickToday = getCountOfSickTodayInInstitute(userInfoDto);
        List<DepartmentCountOfSick> departmentCountOfSicks = getDepartmentCountOfSicks(userInfoDto);
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = getDiseaseTypeCountOfSick(userInfoDto);

        decanatAdditionalInfo.setCountOfDiseasesByDaysForTwoWeeks(countOfDiseasesByDays);
        decanatAdditionalInfo.setCountOfSickNow(countOfSickNow);
        decanatAdditionalInfo.setDepartmentCountOfSicks(departmentCountOfSicks);
        decanatAdditionalInfo.setCountOfRecoverToday(countOfRecoverToday);
        decanatAdditionalInfo.setCountOfSickToday(countOfSickToday);
        decanatAdditionalInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);

        return decanatAdditionalInfo;
    }

    private List<DiseaseTypeCountOfSick> getDiseaseTypeCountOfSick(UserInfoDto userInfoDto) {
        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByInstitute(DiseaseStatus.ACTIVE, instituteId);
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

    private List<DepartmentCountOfSick> getDepartmentCountOfSicks(UserInfoDto userInfoDto) {
        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<InstituteDirection> instituteDirections = instituteDirectionRepository.findAllByInstituteId(instituteId);
        List<Department> departments = getDepartments(instituteDirections);

        return Optional.ofNullable(departments)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::buildDepartmentCountOfSick)
                .collect(Collectors.toList());
    }


    private DepartmentCountOfSick buildDepartmentCountOfSick(Department department) {
        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByDepartment(DiseaseStatus.ACTIVE, department.getId());

        DepartmentCountOfSick departmentCountOfSick = new DepartmentCountOfSick();
        departmentCountOfSick.setDepartmentName(department.getShortName());
        departmentCountOfSick.setCountOfSick(diseaseInformationList.size());

        return departmentCountOfSick;
    }

    private List<Department> getDepartments(List<InstituteDirection> instituteDirections) {
        return Optional.ofNullable(instituteDirections)
                .orElse(Collections.emptyList())
                .stream()
                .map(InstituteDirection::getDepartment)
                .collect(Collectors.toList());
    }

    private String getCountOfSickNowInInstitute(UserInfoDto userInfoDto) {
        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByInstitute(DiseaseStatus.ACTIVE, instituteId);

        return String.valueOf(
                diseaseInformationList.size()
        );
    }

    private String getCountOfRecoverTodayInInstitute(UserInfoDto userInfoDto) {
        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<DiseaseInformation> recoverTodayDiseasesByInstitute = diseaseService.getRecoverTodayDiseasesByInstitute(instituteId);

        return String.valueOf(
                recoverTodayDiseasesByInstitute.size()
        );
    }

    private String getCountOfSickTodayInInstitute(UserInfoDto userInfoDto) {
        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<DiseaseInformation> sickTodayDiseasesByInstitute = diseaseService.getSickTodayDiseasesByInstitute(instituteId);

        return String.valueOf(
                sickTodayDiseasesByInstitute.size()
        );
    }

    private CountOfDiseasesByDays getCountOfDiseasesByDaysForTwoWeeks(UserInfoDto userInfoDto) {
        LocalDate startDate = LocalDate.now().minusDays(TWO_WEEKS_DAYS_COUNT - ONE_DAY);

        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<DiseaseInformation> notRejectedDiseases = diseaseService.getNotRejectedDiseasesByInstitute(instituteId);

        List<Long> countOfDiseasesByTwoWeeks = getCountOfDiseasesByTwoWeeks(notRejectedDiseases, startDate);
        List<String> datesInTwoLatestWeek = getDatesInTwoLatestWeek(startDate);


        CountOfDiseasesByDays countOfDiseasesByDays = new CountOfDiseasesByDays();
        countOfDiseasesByDays.setCountsOfSick(countOfDiseasesByTwoWeeks);
        countOfDiseasesByDays.setDates(datesInTwoLatestWeek);

        return countOfDiseasesByDays;
    }

    private String getDecanatUserInstituteId(UserInfoDto userInfoDto) {
        return Optional.ofNullable(userInfoDto)
                .map(UserInfoDto::getInstitute)
                .map(InstituteResponse::getId)
                .orElse(StringUtils.EMPTY);
    }

    private List<String> getDatesInTwoLatestWeek(LocalDate startDate) {
        List<String> dates = new ArrayList<>();

        IntStream.range(ZERO, TWO_WEEKS_DAYS_COUNT)
                .forEach(dayCount -> {
                    LocalDate date = startDate.plusDays(dayCount);
                    dates.add(DAY_AND_MONTH_DATE_FORMAT.format(date));
                });

        return dates;
    }

    private List<Long> getCountOfDiseasesByTwoWeeks(List<DiseaseInformation> diseaseInformationByTwoWeeks, LocalDate startDate) {
        List<Long> countOfDiseasesByTwoWeeks = new ArrayList<>();

        IntStream.range(ZERO, TWO_WEEKS_DAYS_COUNT)
                .forEach(dayCount -> {
                    LocalDate date = startDate.plusDays(dayCount);
                    Long countOfSick = calculateCountOfSickInDay(date, diseaseInformationByTwoWeeks);
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

    @Override
    public User getUserByLogin(String login) {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new ApiException(ErrorContainer.USER_NOT_FOUND);
        }

        return user;
    }

    @Override
    public User getById(String id) {
        return Optional.of(userRepository.findById(id))
                .get()
                .orElse(null);
    }
}
