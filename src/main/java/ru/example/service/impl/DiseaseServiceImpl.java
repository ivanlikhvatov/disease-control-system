package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.department.Department;
import ru.example.dao.entity.directionProfile.DirectionProfile;
import ru.example.dao.entity.disease.Disease;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.group.Group;
import ru.example.dao.entity.institute.Institute;
import ru.example.dao.entity.instituteDirection.InstituteDirection;
import ru.example.dao.entity.user.User;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.DiseaseResponse;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.DiseaseInfoResponseMapper;
import ru.example.mapper.DiseaseResponseMapper;
import ru.example.repository.DiseaseInformationRepository;
import ru.example.repository.DiseaseRepository;
import ru.example.service.DiseaseService;
import ru.example.utils.Base64Converter;

import java.io.File;
import java.time.LocalDate;
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
    private final DiseaseInfoResponseMapper diseaseInfoResponseMapper;

    @Value("${upload.path}")
    private String uploadPath;


    @Override
    public DiseaseInformation getDiseaseBySickIdAndStatus(String sickId, DiseaseStatus status) {
        return diseaseInformationRepository
                .findByUserIdAndStatus(sickId, status);
    }

    @Override
    public DiseaseInformation getDiseaseInformationById(String id) {
        return Optional.of(diseaseInformationRepository.findById(id))
                .get()
                .orElseThrow(() -> new ApiException(ErrorContainer.DISEASE_NOT_EXIST));
    }

    @Override
    public List<DiseaseResponse> getDiseasesResponse() {
        List<Disease> diseases = diseaseRepository.findAll();
        return diseaseResponseMapper.map(diseases);
    }

    @Override
    public List<Disease> getDiseases() {
        return diseaseRepository.findAll();
    }

    @Override
    public String getUserIdFromDisease(DiseaseInformation diseaseInformation) {
        return Optional.ofNullable(diseaseInformation)
                .map(DiseaseInformation::getUser)
                .map(User::getId)
                .orElse(StringUtils.EMPTY);
    }

    @Override
    public List<DiseaseInformation> getDiseaseFromNeedInstitute(List<DiseaseInformation> diseases, String instituteId) {
        return Optional.ofNullable(diseases)
                .orElse(Collections.emptyList())
                .stream()
                .filter(diseaseInformation -> isNeedInstitute(diseaseInformation, instituteId))
                .collect(Collectors.toList());
    }

    @Override
    public List<DiseaseInformation> getDiseaseFromNeedGroup(List<DiseaseInformation> diseases, String groupId) {
        return Optional.ofNullable(diseases)
                .orElse(Collections.emptyList())
                .stream()
                .filter(diseaseInformation -> isNeedGroup(diseaseInformation, groupId))
                .collect(Collectors.toList());
    }

    @Override
    public List<DiseaseInformation> getDiseaseFromNeedDepartment(List<DiseaseInformation> processedDiseases, String departmentId) {
        return Optional.ofNullable(processedDiseases)
                .orElse(Collections.emptyList())
                .stream()
                .filter(diseaseInformation -> isNeedDepartment(diseaseInformation, departmentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<DiseaseInfoResponse> buildDiseasesResponseWithScannedCertificate(List<DiseaseInformation> processedDiseases) {
        List<DiseaseInfoResponse> response = diseaseInfoResponseMapper.map(processedDiseases);

        return Optional.ofNullable(response)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::addBase64ScannedCertificate)
                .collect(Collectors.toList());
    }

    @Override
    public List<DiseaseInformation> getAllDiseasesInformation() {
        return diseaseInformationRepository.findAll();
    }

    @Override
    public void saveDisease(DiseaseInformation diseaseInformation) {
        diseaseInformationRepository.save(diseaseInformation);
    }

    @Override
    public List<DiseaseInformation> getNotRejectedDiseasesByInstitute(String instituteId) {
        List<DiseaseInformation> notRejectedDiseasesInfo = diseaseInformationRepository.findAllByStatusIsNot(DiseaseStatus.REJECTED);
        return getDiseaseFromNeedInstitute(notRejectedDiseasesInfo, instituteId);
    }

    @Override
    public List<DiseaseInformation> getNotRejectedDiseasesByGroup(String groupId) {
        List<DiseaseInformation> notRejectedDiseasesInfo = diseaseInformationRepository.findAllByStatusIsNot(DiseaseStatus.REJECTED);
        return getDiseaseFromNeedGroup(notRejectedDiseasesInfo, groupId);
    }

    @Override
    public List<DiseaseInformation> getNotRejectedDiseasesByDepartment(String departmentId) {
        List<DiseaseInformation> notRejectedDiseasesInfo = diseaseInformationRepository.findAllByStatusIsNot(DiseaseStatus.REJECTED);
        return getDiseaseFromNeedDepartment(notRejectedDiseasesInfo, departmentId);
    }

    @Override
    public List<DiseaseInformation> getDiseasesInStatus(DiseaseStatus status) {
        return diseaseInformationRepository.findAllByStatus(status);
    }

    @Override
    public List<DiseaseInformation> getDiseasesInStatusByInstitute(DiseaseStatus active, String instituteId) {
        List<DiseaseInformation> diseaseInformationList = diseaseInformationRepository.findAllByStatus(active);
        return getDiseaseFromNeedInstitute(diseaseInformationList, instituteId);
    }

    @Override
    public List<DiseaseInformation> getDiseasesInStatusByGroup(DiseaseStatus active, String groupId) {
        List<DiseaseInformation> diseaseInformationList = diseaseInformationRepository.findAllByStatus(active);
        return getDiseaseFromNeedGroup(diseaseInformationList, groupId);
    }

    @Override
    public List<DiseaseInformation> getDiseasesInStatusByDepartment(DiseaseStatus status, String departmentId) {
        List<DiseaseInformation> diseaseInformationList = diseaseInformationRepository.findAllByStatus(status);
        return getDiseaseFromNeedDepartment(diseaseInformationList, departmentId);
    }

    @Override
    public List<DiseaseInformation> getRecoverTodayDiseasesByInstitute(String instituteId) {
        List<DiseaseInformation> diseaseInformationList = diseaseInformationRepository.findAllByDateOfRecoveryEquals(LocalDate.now());
        return getDiseaseFromNeedInstitute(diseaseInformationList, instituteId);
    }

    @Override
    public List<DiseaseInformation> getSickTodayDiseasesByInstitute(String instituteId) {
        List<DiseaseInformation> diseaseInformationList = diseaseInformationRepository.findAllByDateOfDiseaseEquals(LocalDate.now());
        return getDiseaseFromNeedInstitute(diseaseInformationList, instituteId);
    }

    @Override
    public void deleteScansFromUploads(String scannedCertificateFileName) {
        String path = uploadPath.concat(scannedCertificateFileName);
        File oldScannedCertificate = new File(path);

        if (oldScannedCertificate.delete()) {
            log.debug("Файл с названием: " + scannedCertificateFileName + "успешно удален");
        } else {
            log.debug("Во время удаления файла: " + scannedCertificateFileName + "возникли проблемы");
        }
    }

    private DiseaseInfoResponse addBase64ScannedCertificate(DiseaseInfoResponse diseaseInfo) {

        if (StringUtils.isBlank(diseaseInfo.getScannedCertificateFileName())) {
            return diseaseInfo;
        }

        String fileName = diseaseInfo.getScannedCertificateFileName();
        String path = uploadPath.concat(fileName);

        String pdfInBase64 = Base64Converter.encodePdfFileToBase64(path);
        diseaseInfo.setScannedCertificateInBase64(pdfInBase64);

        return diseaseInfo;
    }

    private boolean isNeedInstitute(DiseaseInformation diseaseInformation, String instituteId) {
        String sickInstituteId = getSickInstituteId(diseaseInformation);
        return instituteId.equals(sickInstituteId);
    }

    private boolean isNeedDepartment(DiseaseInformation diseaseInformation, String departmentId) {
        String sickDepartmentId = getSickDepartmentId(diseaseInformation);
        return departmentId.equals(sickDepartmentId);
    }

    private boolean isNeedGroup(DiseaseInformation diseaseInformation, String groupId) {
        String sickGroupId = getSickGroupId(diseaseInformation);
        return groupId.equals(sickGroupId);
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

    private String getSickDepartmentId(DiseaseInformation diseaseInformation) {
        return Optional.ofNullable(diseaseInformation)
                .map(DiseaseInformation::getUser)
                .map(User::getGroup)
                .map(Group::getDirectionProfile)
                .map(DirectionProfile::getInstituteDirection)
                .map(InstituteDirection::getDepartment)
                .map(Department::getId)
                .orElse(StringUtils.EMPTY);
    }

    private String getSickGroupId(DiseaseInformation diseaseInformation) {
        return Optional.ofNullable(diseaseInformation)
                .map(DiseaseInformation::getUser)
                .map(User::getGroup)
                .map(Group::getId)
                .orElse(StringUtils.EMPTY);
    }
}
