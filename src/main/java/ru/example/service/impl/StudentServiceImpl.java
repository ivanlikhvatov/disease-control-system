package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.disease.AddDiseaseInformationRequest;
import ru.example.dto.request.disease.ApproveDiseaseRequest;
import ru.example.dto.request.disease.ApproveType;
import ru.example.dto.request.disease.EditDiseaseInformationRequest;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.StatusResult;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.DiseaseInfoResponseMapper;
import ru.example.mapper.DiseaseRequestMapper;
import ru.example.repository.UserRepository;
import ru.example.security.jwt.JwtUser;
import ru.example.service.*;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;

    private final DiseaseService diseaseService;
    private final DiseaseInfoResponseMapper diseaseInfoResponseMapper;
    private final DiseaseRequestMapper diseaseRequestMapper;
    private final FileService fileService;
    private final MailSender mailSender;

    @Override
    public DiseaseInfoResponse getActiveDisease(JwtUser jwtUser) {
        User user = getUserByLogin(jwtUser.getLogin());

        DiseaseInformation diseaseInformation = diseaseService
                .getDiseaseBySickIdAndStatus(user.getId(), DiseaseStatus.ACTIVE);

        return diseaseInfoResponseMapper.map(diseaseInformation);
    }

    @Override
    public StatusResult addDiseaseInfo(AddDiseaseInformationRequest request, JwtUser jwtUser) {
        User user = getUserByLogin(jwtUser.getLogin());
        DiseaseInformation diseaseInformation = buildDiseaseInfoBeforeAdd(request, user);

        checkDiseaseInformationBeforeAdd(diseaseInformation);
        diseaseService.saveDisease(diseaseInformation);

        return StatusResult.ok();
    }

    @Override
    public StatusResult editDiseaseInfo(EditDiseaseInformationRequest request, JwtUser jwtUser) {
        User user = getUserByLogin(jwtUser.getLogin());
        DiseaseInformation diseaseInformation = buildDiseaseInfoBeforeEdit(request, user);

        checkDiseaseInformationBeforeEdit(diseaseInformation);
        diseaseService.saveDisease(diseaseInformation);

        return StatusResult.ok();
    }

    @Override
    public StatusResult approveDiseaseBySick(ApproveDiseaseRequest request, JwtUser jwtUser) {
        checkDiseaseInformationBeforeApprove(request, jwtUser);

        User user = getUserByLogin(jwtUser.getLogin());

        if (ApproveType.SCANNED_CERTIFICATE.equals(request.getApproveType())) {
            approveByScannedCertificate(request, user);
        } else {
            approveByElectronicSickId(request, user);
        }

        return StatusResult.ok();
    }


    private void checkDiseaseInformationBeforeAdd(DiseaseInformation diseaseInformation) {
        String userId = diseaseService.getUserIdFromDisease(diseaseInformation);

        Optional.ofNullable(diseaseService.getDiseaseBySickIdAndStatus(userId, DiseaseStatus.ACTIVE))
                .ifPresent(disease -> {
                    throw new ApiException(ErrorContainer.OLD_DISEASE_IS_NOT_CLOSED);
                });

        Optional.ofNullable(diseaseService.getDiseaseBySickIdAndStatus(userId, DiseaseStatus.PROCESSED))
                .ifPresent(disease -> {
                    throw new ApiException(ErrorContainer.OLD_DISEASE_IS_NOT_CLOSED);
                });
    }

    private void checkDiseaseInformationBeforeEdit(DiseaseInformation diseaseInformation) {
        String userId = diseaseService.getUserIdFromDisease(diseaseInformation);
        DiseaseInformation existDiseaseInformation = diseaseService.getDiseaseBySickIdAndStatus(userId, DiseaseStatus.ACTIVE);

        if (existDiseaseInformation == null) {
            throw new ApiException(ErrorContainer.OTHER);
        }

        if (!StringUtils.equals(diseaseInformation.getId(), existDiseaseInformation.getId())) {
            throw new ApiException(ErrorContainer.OTHER);
        }
    }

    private void checkDiseaseInformationBeforeApprove(ApproveDiseaseRequest request, JwtUser jwtUser) {
        String userId = jwtUser.getId();

        DiseaseInformation diseaseInformation = diseaseService
                .getDiseaseBySickIdAndStatus(userId, DiseaseStatus.ACTIVE);

        Optional.ofNullable(diseaseInformation)
                .orElseThrow(() -> new ApiException(ErrorContainer.DISEASE_NOT_EXIST));

        if (!request.getId().equals(diseaseInformation.getId())) {
            throw new ApiException(ErrorContainer.OTHER);
        }
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

    private DiseaseInformation buildDiseaseInfoBeforeApprove(ApproveDiseaseRequest request) {
        DiseaseInformation diseaseInformation = diseaseService.getDiseaseInformationById(request.getId());

        diseaseInformation.setDateOfRecovery(request.getDateOfRecovery());
        diseaseInformation.setApproveType(request.getApproveType());

        if (ApproveType.SCANNED_CERTIFICATE.equals(request.getApproveType())) {
            diseaseInformation.setStatus(DiseaseStatus.PROCESSED);
            String scannedCertificatePath = fileService.createAndSavePdfFileFromBase64(request.getScannedCertificate());;
            diseaseInformation.setScannedCertificateFileName(scannedCertificatePath);
        }

        if (ApproveType.ELECTRONIC_SICK_ID.equals(request.getApproveType())) {
            diseaseInformation.setElectronicSickId(request.getElectronicSickId());
            diseaseInformation.setStatus(DiseaseStatus.APPROVED);
        }

        return diseaseInformation;
    }


    private void approveByScannedCertificate(ApproveDiseaseRequest request, User user) {
        DiseaseInformation diseaseInformation = buildDiseaseInfoBeforeApprove(request);

        diseaseService.saveDisease(diseaseInformation);
        mailSender.sendDiseaseProcessedMessage(user);
    }

    private void approveByElectronicSickId(ApproveDiseaseRequest request, User user) {
        DiseaseInformation diseaseInformation = buildDiseaseInfoBeforeApprove(request);

        diseaseService.saveDisease(diseaseInformation);
        mailSender.sendDiseaseApprovedMessage(user);
    }

    public User getUserByLogin(String login) {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new ApiException(ErrorContainer.USER_NOT_FOUND);
        }

        return user;
    }

    public User getById(String id) {
        return Optional.of(userRepository.findById(id))
                .get()
                .orElse(null);
    }
}
