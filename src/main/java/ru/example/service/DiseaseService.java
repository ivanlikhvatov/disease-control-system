package ru.example.service;

import ru.example.dao.entity.disease.Disease;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.DiseaseResponse;

import java.util.List;

public interface DiseaseService {
    List<DiseaseResponse> getDiseasesResponse();

    List<Disease> getDiseases();

    List<DiseaseInformation> getNotRejectedDiseasesByInstitute(String instituteId);

    List<DiseaseInformation> getDiseasesInStatusByInstitute(DiseaseStatus active, String instituteId);

    List<DiseaseInformation> getDiseasesInStatusByGroup(DiseaseStatus active, String groupId);

    List<DiseaseInformation> getDiseasesInStatus(DiseaseStatus status);

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

    List<DiseaseInformation> getNotRejectedDiseasesByGroup(String groupId);
}
