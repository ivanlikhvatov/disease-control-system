package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.Status;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.user.Role;
import ru.example.dto.request.AuthenticationRequestDto;
import ru.example.dto.response.LoginResponseDto;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.decanatAdditionalInfo.CountOfDiseasesByDays;
import ru.example.dto.response.decanatAdditionalInfo.DecanatAdditionalInfoResponse;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.LoginResponseDtoMapper;
import ru.example.repository.DiseaseInformationRepository;
import ru.example.security.jwt.JwtTokenProvider;
import ru.example.service.AuthenticationService;
import ru.example.service.DiseaseService;
import ru.example.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final LoginResponseDtoMapper mapper;
    private final DiseaseService diseaseService;


    //TODO почему здесь возвращаю LoginResponseDto а не AuthenticationRequestDto (потому что userInfo возвращается в сервисах по запросу не личной информации(не содержит токен))
    @Override
    public LoginResponseDto loginUser(AuthenticationRequestDto request) {
        String login = request.getLogin();
        String password = request.getPassword();

        UserInfoDto user = userService.getUserInfoDtoByLogin(login);

        checkUser(user);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(login, password);

        authenticateUser(authenticationToken);

        String token = jwtTokenProvider.createToken(request.getLogin(), user.getRoles());

        LoginResponseDto loginResponseDto = mapper.map(user, token);
        buildWithAdditionalInfoByRole(loginResponseDto);

        return loginResponseDto;
    }

    private void buildWithAdditionalInfoByRole(LoginResponseDto loginResponseDto) {
        if (loginResponseDto.getRoles().contains(Role.DECANAT)) {
            addDecanatAdditionalInfo(loginResponseDto);
        }
    }

    private void addDecanatAdditionalInfo(LoginResponseDto loginResponseDto) {
        DecanatAdditionalInfoResponse decanatAdditionalInfo = new DecanatAdditionalInfoResponse();

        addCountOfDiseasesByDaysForTwoWeeks(loginResponseDto, decanatAdditionalInfo);
        addCountOfSickNow(decanatAdditionalInfo);
        loginResponseDto.setDecanatAdditionalInfo(decanatAdditionalInfo);
    }

    private void addCountOfSickNow(DecanatAdditionalInfoResponse decanatAdditionalInfo) {
        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatus(DiseaseStatus.ACTIVE);
        //TODO
        int countOfSickNow = diseaseInformationList.size();
        decanatAdditionalInfo.setCountOfSickToday(String.valueOf(countOfSickNow));

    }

    private void addCountOfDiseasesByDaysForTwoWeeks(LoginResponseDto loginResponseDto, DecanatAdditionalInfoResponse decanatAdditionalInfo) {
        LocalDate startDate = LocalDate.now().minusDays(13); //TODO

        //TODO npe
        List<DiseaseInformation> diseaseInformationByTwoWeeks = diseaseService.getDiseasesByInstituteAndDuration(loginResponseDto.getInstitute().getId(), startDate);

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

        System.out.println("diseaseInformationByTwoWeeks: " + diseaseInformationByTwoWeeks);

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

    private void authenticateUser(UsernamePasswordAuthenticationToken authenticationToken) {
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(ErrorContainer.USER_NOT_FOUND);
        }
    }

    private void checkUser(UserInfoDto user) {
        if (!Status.ACTIVE.equals(user.getStatus())){
            throw new ApiException(ErrorContainer.USER_STATUS_NOT_ACTIVE);
        }
    }
}
