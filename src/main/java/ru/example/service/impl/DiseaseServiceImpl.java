package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.directionProfile.DirectionProfile;
import ru.example.dao.entity.disease.Disease;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.group.Group;
import ru.example.dao.entity.institute.Institute;
import ru.example.dao.entity.instituteDirection.InstituteDirection;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.disease.ApproveDiseaseRequest;
import ru.example.dto.request.disease.AddDiseaseInformationRequest;
import ru.example.dto.request.disease.ApproveType;
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
import ru.example.service.FileService;
import ru.example.service.MailSender;
import ru.example.service.UserService;
import ru.example.utils.Base64Converter;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final DiseaseInformationRepository diseaseInformationRepository;
    private final DiseaseResponseMapper diseaseResponseMapper;
    private final DiseaseRequestMapper diseaseRequestMapper;
    private final DiseaseInfoResponseMapper diseaseInfoResponseMapper;
    private final UserService userService;
    private final MailSender mailSender;
    private final FileService fileService;

    @Value("${upload.path}")
    private String uploadPath;

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
    public StatusResult approveDiseaseBySick(ApproveDiseaseRequest request, JwtUser jwtUser) {
        validateApproveDiseaseRequest(request, jwtUser);

        User user = userService.getUserByLogin(jwtUser.getLogin());

        if (ApproveType.SCANNED_CERTIFICATE.equals(request.getApproveType())) {
            approveByScannedCertificate(request, user);
        } else {
            approveByElectronicSickId(request, user);
        }

        return StatusResult.ok();
    }

    @Override
    public List<DiseaseInfoResponse> getProcessedDiseases(JwtUser jwtUser) {

        List<DiseaseInformation> processedDiseases = diseaseInformationRepository
                .findAllByStatus(DiseaseStatus.PROCESSED);

        User decanatUser = userService.getUserByLogin(jwtUser.getLogin());

        List<DiseaseInformation> diseaseFromNeedInstitute = getDiseaseFromNeedInstitute(processedDiseases, decanatUser);

        return buildDiseasesResponseWithScannedCertificate(diseaseFromNeedInstitute);
    }

    @Override
    public StatusResult approveDiseaseByDecanat(String diseaseId, JwtUser jwtUser) {
        DiseaseInformation diseaseInformation = findDiseaseById(diseaseId);
        User approvedAuthor = userService.getUserByLogin(jwtUser.getLogin());

        diseaseInformation.setStatus(DiseaseStatus.APPROVED);
        diseaseInformation.setApproveAuthor(approvedAuthor);

        diseaseInformationRepository.save(diseaseInformation);
        sendNotificationAboutDiseaseApprove(diseaseInformation);

        return StatusResult.ok();
    }

    @Override
    public StatusResult refundDiseaseToStudent(String diseaseId, String refundCause) {

        DiseaseInformation diseaseInformation = findDiseaseById(diseaseId);
        diseaseInformation.setStatus(DiseaseStatus.ACTIVE);
        diseaseInformation.setDateOfRecovery(null);
        deleteFileFromUploads(diseaseInformation.getScannedCertificateFileName());
        diseaseInformation.setScannedCertificateFileName(null);

        diseaseInformationRepository.save(diseaseInformation);
        sendNotificationAboutDiseaseRefund(diseaseInformation, refundCause);

        return StatusResult.ok();
    }

    @Override
    public StatusResult rejectDisease(String diseaseId, String rejectCause, JwtUser jwtUser) {

        User rejectAuthor = userService.getUserByLogin(jwtUser.getLogin());

        DiseaseInformation diseaseInformation = findDiseaseById(diseaseId);
        diseaseInformation.setStatus(DiseaseStatus.REJECTED);
        diseaseInformation.setRejectAuthor(rejectAuthor);
        diseaseInformation.setRejectCause(rejectCause);

        diseaseInformationRepository.save(diseaseInformation);
        sendNotificationAboutDiseaseReject(diseaseInformation, rejectCause);

        return StatusResult.ok();
    }

    private void sendNotificationAboutDiseaseReject(DiseaseInformation diseaseInformation, String rejectCause) {
        String userId = getUserId(diseaseInformation);
        User user = userService.getById(userId);

        mailSender.sendDiseaseRejectMessage(user, rejectCause);
    }

    private void sendNotificationAboutDiseaseRefund(DiseaseInformation diseaseInformation, String refundCause) {
        String userId = getUserId(diseaseInformation);
        User user = userService.getById(userId);

        mailSender.sendDiseaseRefundToUserMessage(user, refundCause);
    }

    private void deleteFileFromUploads(String scannedCertificateFileName) {
        String path = uploadPath.concat(scannedCertificateFileName);
        File oldScannedCertificate = new File(path);

        if (oldScannedCertificate.delete()) {
            log.debug("Файл с названием: " + scannedCertificateFileName + "успешно удален");
        } else {
            log.debug("Во время удаления файла: " + scannedCertificateFileName + "возникли проблемы");
        }
    }

    private void sendNotificationAboutDiseaseApprove(DiseaseInformation diseaseInformation) {
        String userId = getUserId(diseaseInformation);
        User user = userService.getById(userId);
        mailSender.sendDiseaseApprovedMessage(user);
    }

    private List<DiseaseInformation> getDiseaseFromNeedInstitute(List<DiseaseInformation> processedDiseases, User decanatUser) {
        return Optional.ofNullable(processedDiseases)
                .orElse(Collections.emptyList())
                .stream()
                .filter(diseaseInformation -> isDecanatInstitute(diseaseInformation, decanatUser))
                .collect(Collectors.toList());
    }

    private boolean isDecanatInstitute(DiseaseInformation diseaseInformation, User decanatUser) {
        String sickInstituteId = getSickInstituteId(diseaseInformation);
        String decanatInstituteId = getDecanatInstituteId(decanatUser);

        return decanatInstituteId.equals(sickInstituteId);
    }

    private String getDecanatInstituteId(User decanatUser) {
        return Optional.ofNullable(decanatUser)
                .map(User::getInstitute)
                .map(Institute::getId)
                .orElse(StringUtils.EMPTY);
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

    private List<DiseaseInfoResponse> buildDiseasesResponseWithScannedCertificate(List<DiseaseInformation> processedDiseases) {
        List<DiseaseInfoResponse> response = diseaseInfoResponseMapper.map(processedDiseases);

        return Optional.ofNullable(response)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::addBase64ScannedCertificate)
                .collect(Collectors.toList());
    }

    private DiseaseInfoResponse addBase64ScannedCertificate(DiseaseInfoResponse diseaseInfo) {
        String fileName = diseaseInfo.getScannedCertificateFileName();
        String path = uploadPath.concat(fileName);

        String pdfInBase64 = Base64Converter.encodePdfFileToBase64(path);
        diseaseInfo.setScannedCertificateInBase64(pdfInBase64);

        return diseaseInfo;
    }

    private void approveByScannedCertificate(ApproveDiseaseRequest request, User user) {
        DiseaseInformation diseaseInformation = buildDiseaseInfoBeforeApprove(request);

        diseaseInformationRepository.save(diseaseInformation);
        mailSender.sendDiseaseProcessedMessage(user);
    }

    private void approveByElectronicSickId(ApproveDiseaseRequest request, User user) {
        DiseaseInformation diseaseInformation = buildDiseaseInfoBeforeApprove(request);

        diseaseInformationRepository.save(diseaseInformation);
        mailSender.sendDiseaseApprovedMessage(user);
    }

    private DiseaseInformation buildDiseaseInfoBeforeApprove(ApproveDiseaseRequest request) {
        DiseaseInformation diseaseInformation = findDiseaseById(request.getId());

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

    private DiseaseInformation findDiseaseById(String id) {
        return Optional.of(diseaseInformationRepository.findById(id))
                .get()
                .orElseThrow(() -> new ApiException(ErrorContainer.DISEASE_NOT_EXIST));
    }

    private void validateApproveDiseaseRequest(ApproveDiseaseRequest request, JwtUser jwtUser) {
        String userId = jwtUser.getId();

        DiseaseInformation diseaseInformation = diseaseInformationRepository
                .findByUserIdAndStatus(userId, DiseaseStatus.ACTIVE);

        Optional.ofNullable(diseaseInformation)
                .orElseThrow(() -> new ApiException(ErrorContainer.DISEASE_NOT_EXIST));

        if (!request.getId().equals(diseaseInformation.getId())) {
            throw new ApiException(ErrorContainer.OTHER);
        }
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

        Optional.ofNullable(diseaseInformationRepository.findByUserIdAndStatus(userId, DiseaseStatus.ACTIVE))
                .ifPresent(disease -> {
                    throw new ApiException(ErrorContainer.OLD_DISEASE_IS_NOT_CLOSED);
                });

        Optional.ofNullable(diseaseInformationRepository.findByUserIdAndStatus(userId, DiseaseStatus.PROCESSED))
                .ifPresent(disease -> {
                    throw new ApiException(ErrorContainer.OLD_DISEASE_IS_NOT_CLOSED);
                });
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
