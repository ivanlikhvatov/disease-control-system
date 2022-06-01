package ru.example.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.directionProfile.DirectionProfile;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.group.Group;
import ru.example.dao.entity.institute.Institute;
import ru.example.dao.entity.instituteDirection.InstituteDirection;
import ru.example.dao.entity.user.Role;
import ru.example.dao.entity.user.User;
import ru.example.dto.response.LoginResponseDto;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.decanatAdditionalInfo.CountOfDiseasesByDays;
import ru.example.dto.response.decanatAdditionalInfo.DecanatAdditionalInfoResponse;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.UserInfoResponseDtoMapper;
import ru.example.repository.DiseaseInformationRepository;
import ru.example.repository.DiseaseRepository;
import ru.example.repository.UserRepository;
import ru.example.service.DiseaseService;
import ru.example.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserInfoResponseDtoMapper mapper;
    private final DiseaseInformationRepository diseaseInformationRepository;

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
        buildWithAdditionalInfoByRole(userInfoDto);


        return userInfoDto;
    }


    //TODO вынести
    private void buildWithAdditionalInfoByRole(UserInfoDto userInfoDto) {
        if (userInfoDto.getRoles().contains(Role.DECANAT)) {
            addDecanatAdditionalInfo(userInfoDto);
        }
    }

    private void addDecanatAdditionalInfo(UserInfoDto userInfoDto) {
        DecanatAdditionalInfoResponse decanatAdditionalInfo = new DecanatAdditionalInfoResponse();

        addCountOfDiseasesByDaysForTwoWeeks(userInfoDto, decanatAdditionalInfo);
        addCountOfSickNow(decanatAdditionalInfo);
        userInfoDto.setDecanatAdditionalInfo(decanatAdditionalInfo);
    }

    private void addCountOfSickNow(DecanatAdditionalInfoResponse decanatAdditionalInfo) {
        List<DiseaseInformation> diseaseInformationList = diseaseInformationRepository.findAllByStatus(DiseaseStatus.ACTIVE);
        //TODO
        int countOfSickNow = diseaseInformationList.size();
        decanatAdditionalInfo.setCountOfSickToday(String.valueOf(countOfSickNow));

    }

    private void addCountOfDiseasesByDaysForTwoWeeks(UserInfoDto userInfoDto, DecanatAdditionalInfoResponse decanatAdditionalInfo) {
        LocalDate startDate = LocalDate.now().minusDays(13); //TODO

        //TODO npe
        List<DiseaseInformation> diseaseInformationByTwoWeeks = getDiseasesByInstituteAndDuration(userInfoDto.getInstitute().getId(), startDate);

        List<Long> countOfDiseasesByTwoWeeks = getCountOfDiseasesByTwoWeeks(diseaseInformationByTwoWeeks, startDate);
        List<String> datesInTwoLatestWeek = getDatesInTwoLatestWeek(startDate);


        CountOfDiseasesByDays countOfDiseasesByDays = new CountOfDiseasesByDays();
        countOfDiseasesByDays.setCountsOfSick(countOfDiseasesByTwoWeeks);
        countOfDiseasesByDays.setDates(datesInTwoLatestWeek);

        decanatAdditionalInfo.setCountOfDiseasesByDaysForTwoWeeks(countOfDiseasesByDays);
    }

    private List<String> getDatesInTwoLatestWeek(LocalDate startDate) {
        List<String> dates = new ArrayList<>();

        for (int i = 0; i <= 13; i++) {
            LocalDate date = startDate.plusDays(i);
            //TODO
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
            dates.add(formatter.format(date));
        }

        return dates;
    }

    private List<Long> getCountOfDiseasesByTwoWeeks(List<DiseaseInformation> diseaseInformationByTwoWeeks, LocalDate startDate) {
        List<Long> countOfDiseasesByTwoWeeks = new ArrayList<>();

        //TODO переделать
        for (int i = 0; i <= 13; i++) {
            LocalDate date = startDate.plusDays(i);
            Long countOfSick = calculateCountOfSickInDay(date, diseaseInformationByTwoWeeks);
            countOfDiseasesByTwoWeeks.add(countOfSick);
        }

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

    //TODO вынести ^

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

    //TODO удалить! добавил из-за того что появилась зиклическая заыисимость между diseaseService и userService

    public List<DiseaseInformation> getDiseasesByInstituteAndDuration(String instituteId, LocalDate startDate) {
        List<DiseaseInformation> diseaseInformationActiveAfterStartDate = diseaseInformationRepository.findAllByStatusIsNot(DiseaseStatus.REJECTED);
        return getDiseaseFromNeedInstitute(diseaseInformationActiveAfterStartDate, instituteId);
    }

    private List<DiseaseInformation> getDiseaseFromNeedInstitute(List<DiseaseInformation> processedDiseases, String instituteId) {
        return Optional.ofNullable(processedDiseases)
                .orElse(Collections.emptyList())
                .stream()
                .filter(diseaseInformation -> isDecanatInstitute(diseaseInformation, instituteId))
                .collect(Collectors.toList());
    }

    private boolean isDecanatInstitute(DiseaseInformation diseaseInformation, String instituteId) {
        String sickInstituteId = getSickInstituteId(diseaseInformation);
        return instituteId.equals(sickInstituteId);
    }

    private String getSickInstituteId(DiseaseInformation diseaseInformation) {
        return Optional.ofNullable(diseaseInformation)
                .map(DiseaseInformation::getUser)
                .map(User::getGroup)
                .map(Group::getDirectionProfile)
                .map(DirectionProfile::getInstituteDirection)
                .map(InstituteDirection::getInstitute)
                .map(Institute::getId)
                .orElse(StringUtils.EMPTY);
    }
}
