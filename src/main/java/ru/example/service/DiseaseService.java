package ru.example.service;

import ru.example.dao.entity.disease.Disease;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.DiseaseResponse;
import ru.example.dto.response.UserInfoDto;

import java.util.List;
import java.util.Optional;

public interface DiseaseService {

    List<Disease> getDiseases();

    List<DiseaseResponse> getDiseasesResponse();

    List<DiseaseInformation> getAllNotRejectedDiseases();

    List<DiseaseInformation> getNotRejectedDiseasesByInstitute(String instituteId);

    List<DiseaseInformation> getNotRejectedDiseasesByDepartment(String departmentId);

    List<DiseaseInformation> getNotRejectedDiseasesByGroup(String groupId);

    List<DiseaseInformation> getDiseasesInStatusByInstitute(DiseaseStatus active, String instituteId);

    List<DiseaseInformation> getAllDiseasesInStatus(DiseaseStatus status);

    List<DiseaseInformation> getDiseasesInStatusByGroup(DiseaseStatus active, String groupId);

    List<DiseaseInformation> getDiseasesInStatusByDepartment(DiseaseStatus status, String departmentId);

    List<DiseaseInformation> getRecoverTodayDiseasesByInstitute(String instituteId);

    List<DiseaseInformation> getSickTodayDiseasesByInstitute(String instituteId);

    DiseaseInformation getDiseaseBySickIdAndStatus(String sickId, DiseaseStatus status);

    String getUserIdFromDisease(DiseaseInformation diseaseInformation);

    List<DiseaseInformation> getDiseaseFromNeedInstitute(List<DiseaseInformation> processedDiseases, String decanatInstituteId);

    List<DiseaseInformation> getDiseaseFromNeedDepartment(List<DiseaseInformation> processedDiseases, String departmentId);

    List<DiseaseInformation> getDiseaseFromNeedGroup(List<DiseaseInformation> diseases, String groupId);

    List<DiseaseInfoResponse> buildDiseasesResponseWithScannedCertificate(List<DiseaseInformation> processedDiseases);

    List<DiseaseInformation> getAllDiseasesInformation();

    DiseaseInformation getDiseaseInformationById(String id);

    void saveDisease(DiseaseInformation diseaseInformation);

    void deleteScansFromUploads(String scannedCertificateFileName);

    List<DiseaseInformation> getAllRecoverTodayDiseases();

    List<DiseaseInformation> getAllSickTodayDiseases();

    List<DiseaseInformation> getAllDiseasesByGroup(String groupId);

    List<DiseaseInformation> getRecoverTodayByGroup(String groupId);

    List<DiseaseInformation> getSickTodayDiseasesByGroup(String groupId);
}
