package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.disease.Disease;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.disease.DiseaseInformationRequest;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.DiseaseResponse;
import ru.example.dto.response.StatusResult;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.DiseaseInfoResponseMapper;
import ru.example.mapper.DiseaseRequestMapper;
import ru.example.mapper.DiseaseResponseMapper;
import ru.example.repository.DiseaseInformationRepository;
import ru.example.repository.DiseaseRepository;
import ru.example.security.jwt.JwtUser;
import ru.example.service.DiseaseService;
import ru.example.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final DiseaseInformationRepository diseaseInformationRepository;
    private final DiseaseResponseMapper diseaseResponseMapper;
    private final DiseaseRequestMapper diseaseRequestMapper;
    private final DiseaseInfoResponseMapper diseaseInfoResponseMapper;
    private final UserService userService;

    @Override
    public List<DiseaseResponse> getDiseases() {
        List<Disease> diseases = diseaseRepository.findAll();
        return diseaseResponseMapper.map(diseases);
    }

    @Override
    public StatusResult addDiseaseInfo(DiseaseInformationRequest request, JwtUser jwtUser) {
        User user = userService.getUserByLogin(jwtUser.getLogin());
        DiseaseInformation diseaseInformation = buildDiseaseInfo(request, user);

        checkDiseaseInformation(diseaseInformation);
        diseaseInformationRepository.save(diseaseInformation);

        return StatusResult.ok();
    }

    @Override
    public DiseaseInfoResponse getNotClosedDisease(JwtUser jwtUser) {
        User user = userService.getUserByLogin(jwtUser.getLogin());

        DiseaseInformation diseaseInformation = diseaseInformationRepository
                .findByUserIdAndIsClosed(user.getId(), Boolean.FALSE);

        return diseaseInfoResponseMapper.map(diseaseInformation);
    }

    private DiseaseInformation buildDiseaseInfo(DiseaseInformationRequest request, User user) {
        DiseaseInformation diseaseInformation = diseaseRequestMapper.map(request);
        diseaseInformation.setIsApproved(Boolean.FALSE);
        diseaseInformation.setIsClosed(Boolean.FALSE);
        diseaseInformation.setUser(user);

        return diseaseInformation;
    }

    private void checkDiseaseInformation(DiseaseInformation diseaseInformation) {
        String userId = getUserId(diseaseInformation);
        DiseaseInformation existDiseaseInformation = diseaseInformationRepository.findByUserIdAndIsClosed(userId, Boolean.FALSE);

        if (existDiseaseInformation != null) {
            throw new ApiException(ErrorContainer.OLD_DISEASE_IS_NOT_CLOSED);
        }
    }

    private String getUserId(DiseaseInformation diseaseInformation) {
        return Optional.ofNullable(diseaseInformation)
                .map(DiseaseInformation::getUser)
                .map(User::getId)
                .orElse(StringUtils.EMPTY);
    }
}
