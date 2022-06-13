package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.user.Role;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.creationService.GroupRequest;
import ru.example.dto.request.graphics.GroupGraphicRequest;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.graphics.CountOfDiseasesByDays;
import ru.example.dto.response.graphics.GroupGraphicInfo;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.UserInfoResponseDtoMapper;
import ru.example.repository.UserRepository;
import ru.example.security.jwt.JwtUser;
import ru.example.service.DiseaseService;
import ru.example.service.GraphicsService;
import ru.example.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GraphicsServiceImpl implements GraphicsService {

    private final static int ONE_DAY = 1;
    private final static DateTimeFormatter DAY_AND_MONTH_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM");

    private final UserRepository userRepository;
    private final DiseaseService diseaseService;

    private final UserInfoResponseDtoMapper userInfoResponseDtoMapper;

    @Override
    public GroupGraphicInfo getGroupGraphicInfo(GroupGraphicRequest groupGraphicRequest, JwtUser jwtUser) {
        User user = getUserByLogin(jwtUser.getLogin());
        UserInfoDto userInfoDto = userInfoResponseDtoMapper.map(user);

        if (user.getRoles().contains(Role.DECANAT)) {
            return buildDecanatGroupGraphicInfo(userInfoDto, groupGraphicRequest);
        }

        return new GroupGraphicInfo();
    }

    private GroupGraphicInfo buildDecanatGroupGraphicInfo(UserInfoDto userInfoDto, GroupGraphicRequest groupGraphicRequest) {

        GroupGraphicInfo groupGraphicInfo = new GroupGraphicInfo();
        CountOfDiseasesByDays countOfDiseasesByDays = buildCountOfDiseasesByDaysForGroupRequest(groupGraphicRequest);

        groupGraphicInfo.setCountOfDiseasesByDaysInGroup(countOfDiseasesByDays);

        return groupGraphicInfo;
    }

    private CountOfDiseasesByDays buildCountOfDiseasesByDaysForGroupRequest(GroupGraphicRequest groupGraphicRequest) {
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
