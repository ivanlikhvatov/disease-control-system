package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.disease.Disease;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.ApproveDiseaseRequest;
import ru.example.dto.request.disease.AddDiseaseInformationRequest;
import ru.example.dto.request.disease.EditDiseaseInformationRequest;
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
    public StatusResult addDiseaseInfo(AddDiseaseInformationRequest request, JwtUser jwtUser) {
        User user = userService.getUserByLogin(jwtUser.getLogin());
        DiseaseInformation diseaseInformation = buildDiseaseInfoBeforeAdd(request, user);

        checkDiseaseInformationBeforeAdd(diseaseInformation);
        diseaseInformationRepository.save(diseaseInformation);

        return StatusResult.ok();
    }

    @Override
    public StatusResult editDiseaseInfo(EditDiseaseInformationRequest request, JwtUser jwtUser) {
        User user = userService.getUserByLogin(jwtUser.getLogin());
        DiseaseInformation diseaseInformation = buildDiseaseInfoBeforeEdit(request, user);

        checkDiseaseInformationBeforeEdit(diseaseInformation);
        diseaseInformationRepository.save(diseaseInformation);

        return StatusResult.ok();
    }

    @Override
    public StatusResult approveDisease(ApproveDiseaseRequest request, JwtUser jwtUser) {
        return null;
    }

    @Override
    public DiseaseInfoResponse getActiveDisease(JwtUser jwtUser) {
        User user = userService.getUserByLogin(jwtUser.getLogin());

        DiseaseInformation diseaseInformation = diseaseInformationRepository
                .findByUserIdAndStatus(user.getId(), DiseaseStatus.ACTIVE);

        return diseaseInfoResponseMapper.map(diseaseInformation);
    }

    private DiseaseInformation buildDiseaseInfoBeforeAdd(AddDiseaseInformationRequest request, User user) {
        DiseaseInformation diseaseInformation = diseaseRequestMapper.map(request);
        diseaseInformation.setStatus(DiseaseStatus.ACTIVE);
        diseaseInformation.setUser(user);

        return diseaseInformation;
    }

    private DiseaseInformation buildDiseaseInfoBeforeEdit(EditDiseaseInformationRequest request, User user) {
        DiseaseInformation diseaseInformation = diseaseRequestMapper.map(request);
        diseaseInformation.setStatus(DiseaseStatus.ACTIVE);
        diseaseInformation.setUser(user);

        return diseaseInformation;
    }

    private void checkDiseaseInformationBeforeAdd(DiseaseInformation diseaseInformation) {
        String userId = getUserId(diseaseInformation);
        DiseaseInformation existDiseaseInformation = diseaseInformationRepository.findByUserIdAndStatus(userId, DiseaseStatus.ACTIVE);

        if (existDiseaseInformation != null) {
            throw new ApiException(ErrorContainer.OLD_DISEASE_IS_NOT_CLOSED);
        }
    }

    private void checkDiseaseInformationBeforeEdit(DiseaseInformation diseaseInformation) {
        String userId = getUserId(diseaseInformation);
        DiseaseInformation existDiseaseInformation = diseaseInformationRepository.findByUserIdAndStatus(userId, DiseaseStatus.ACTIVE);

        if (existDiseaseInformation == null) {
            throw new ApiException(ErrorContainer.OTHER);
        }

        if (!StringUtils.equals(diseaseInformation.getId(), existDiseaseInformation.getId())) {
            throw new ApiException(ErrorContainer.OTHER);
        }
    }

    private String getUserId(DiseaseInformation diseaseInformation) {
        return Optional.ofNullable(diseaseInformation)
                .map(DiseaseInformation::getUser)
                .map(User::getId)
                .orElse(StringUtils.EMPTY);
    }
}
