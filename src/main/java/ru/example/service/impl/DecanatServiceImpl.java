package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.disease.DiseaseInformation;
import ru.example.dao.entity.disease.DiseaseStatus;
import ru.example.dao.entity.institute.Institute;
import ru.example.dao.entity.user.User;
import ru.example.dto.response.*;
import ru.example.dto.response.graphics.CountOfDiseasesByDays;
import ru.example.dto.response.additionalInfo.DecanatAdditionalInfo;
import ru.example.dto.response.graphics.UniversityPartCountOfSick;
import ru.example.dto.response.graphics.DiseaseTypeCountOfSick;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.DiseaseInfoResponseMapper;
import ru.example.repository.UserRepository;
import ru.example.security.jwt.JwtUser;
import ru.example.service.*;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DecanatServiceImpl implements DecanatService {

    private final static int TWO_WEEKS_DAYS_COUNT = 14;
    private final static int ONE_DAY = 1;

    private final DiseaseInfoResponseMapper diseaseInfoResponseMapper;
    private final DiseaseService diseaseService;
    private final MailSender mailSender;
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final DepartmentService departmentService;
    private final GraphicsService graphicsService;

    @Override
    public List<DiseaseInfoResponse> getProcessedDiseasesByInsitute(JwtUser jwtUser) {

        List<DiseaseInformation> processedDiseases = diseaseService
                .getAllDiseasesInStatus(DiseaseStatus.PROCESSED);

        User decanatUser = getUserByLogin(jwtUser.getLogin());
        String decanatInstituteId = getDecanatInstituteId(decanatUser);

        List<DiseaseInformation> diseaseFromNeedInstitute = diseaseService.getDiseaseFromNeedInstitute(processedDiseases, decanatInstituteId);

        return diseaseService.buildDiseasesResponseWithScannedCertificate(diseaseFromNeedInstitute);
    }

    @Override
    public List<DiseaseInfoResponse> getActiveDiseasesByInstitute(JwtUser jwtUser) {
        List<DiseaseInformation> activeDiseases = diseaseService
                .getAllDiseasesInStatus(DiseaseStatus.ACTIVE);

        User decanatUser = getUserByLogin(jwtUser.getLogin());
        String decanatInstituteId = getDecanatInstituteId(decanatUser);

        List<DiseaseInformation> diseaseFromNeedInstitute = diseaseService.getDiseaseFromNeedInstitute(activeDiseases, decanatInstituteId);

        return diseaseInfoResponseMapper.map(diseaseFromNeedInstitute);
    }

    @Override
    public List<DiseaseInfoResponse> getAllDiseaseInformationByInstitute(JwtUser jwtUser) {

        User decanatUser = getUserByLogin(jwtUser.getLogin());
        String decanatInstituteId = getDecanatInstituteId(decanatUser);

        List<DiseaseInformation> diseaseInformationList = diseaseService.getAllDiseasesInformation();
        List<DiseaseInformation> diseasesInformationFromInstitute = diseaseService.getDiseaseFromNeedInstitute(diseaseInformationList, decanatInstituteId);

        return diseaseService.buildDiseasesResponseWithScannedCertificate(diseasesInformationFromInstitute);
    }

    @Override
    public StatusResult approveDiseaseByDecanat(String diseaseId, JwtUser jwtUser) {
        DiseaseInformation diseaseInformation = diseaseService.getDiseaseInformationById(diseaseId);
        User approvedAuthor = getUserByLogin(jwtUser.getLogin());

        diseaseInformation.setStatus(DiseaseStatus.APPROVED);
        diseaseInformation.setApproveAuthor(approvedAuthor);

        diseaseService.saveDisease(diseaseInformation);
        sendNotificationAboutDiseaseApprove(diseaseInformation);

        return StatusResult.ok();
    }

    @Override
    public StatusResult refundDiseaseToStudent(String diseaseId, String refundCause) {

        DiseaseInformation diseaseInformation = diseaseService.getDiseaseInformationById(diseaseId);
        diseaseInformation.setStatus(DiseaseStatus.ACTIVE);
        diseaseInformation.setDateOfRecovery(null);
        diseaseService.deleteScansFromUploads(diseaseInformation.getScannedCertificateFileName());
        diseaseInformation.setScannedCertificateFileName(null);

        diseaseService.saveDisease(diseaseInformation);
        sendNotificationAboutDiseaseRefund(diseaseInformation, refundCause);

        return StatusResult.ok();
    }

    @Override
    public StatusResult rejectDisease(String diseaseId, String rejectCause, JwtUser jwtUser) {

        User rejectAuthor = getUserByLogin(jwtUser.getLogin());

        DiseaseInformation diseaseInformation = diseaseService.getDiseaseInformationById(diseaseId);
        diseaseInformation.setStatus(DiseaseStatus.REJECTED);
        diseaseInformation.setRejectAuthor(rejectAuthor);
        diseaseInformation.setRejectCause(rejectCause);

        diseaseService.saveDisease(diseaseInformation);
        sendNotificationAboutDiseaseReject(diseaseInformation, rejectCause);

        return StatusResult.ok();
    }

    @Override
    public DecanatAdditionalInfo buildDecanatAdditionalInfo(UserInfoDto userInfoDto) {
        DecanatAdditionalInfo decanatAdditionalInfo = new DecanatAdditionalInfo();

        CountOfDiseasesByDays countOfDiseasesByDays = getCountOfDiseasesByDaysForTwoWeeks(userInfoDto);
        String countOfSickNow = getCountOfSickNowInInstitute(userInfoDto);
        String countOfRecoverToday = getCountOfRecoverTodayInInstitute(userInfoDto);
        String countOfSickToday = getCountOfSickTodayInInstitute(userInfoDto);
        List<UniversityPartCountOfSick> departmentCountOfSicks = graphicsService.buildDepartmentCountOfSicksForDecanat(userInfoDto);
        List<DiseaseTypeCountOfSick> diseaseTypeCountOfSicks = getDiseasesByTypeCountOfSickInInstitute(userInfoDto);

        decanatAdditionalInfo.setCountOfDiseasesByDaysForTwoWeeks(countOfDiseasesByDays);
        decanatAdditionalInfo.setCountOfSickNow(countOfSickNow);
        decanatAdditionalInfo.setUniversityPartCountOfSicks(departmentCountOfSicks);
        decanatAdditionalInfo.setCountOfRecoverToday(countOfRecoverToday);
        decanatAdditionalInfo.setCountOfSickToday(countOfSickToday);
        decanatAdditionalInfo.setDiseaseTypeCountOfSicks(diseaseTypeCountOfSicks);

        return decanatAdditionalInfo;
    }

    @Override
    public UniversityInfo buildUniversityInfo(UserInfoDto userInfoDto) {

        UniversityInfo universityInfo = new UniversityInfo();
        User decanatUser = getUserByLogin(userInfoDto.getLogin());
        String instituteId = getDecanatInstituteId(decanatUser);

        List<GroupResponse> groups = groupService.getAllGroupsByInstituteId(instituteId);
        List<DepartmentResponse> departments = departmentService.getAllDepartmentsByInstituteId(instituteId);

        universityInfo.setDepartments(departments);
        universityInfo.setGroups(groups);

        return universityInfo;
    }

    private List<DiseaseTypeCountOfSick> getDiseasesByTypeCountOfSickInInstitute(UserInfoDto userInfoDto) {
        String instituteId = getDecanatUserInstituteId(userInfoDto);
        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByInstitute(DiseaseStatus.ACTIVE, instituteId);

        return graphicsService.buildCountOfDiseasesByType(diseaseInformationList);
    }

    private String getCountOfSickNowInInstitute(UserInfoDto userInfoDto) {
        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<DiseaseInformation> diseaseInformationList = diseaseService.getDiseasesInStatusByInstitute(DiseaseStatus.ACTIVE, instituteId);

        return String.valueOf(
                diseaseInformationList.size()
        );
    }

    private String getCountOfRecoverTodayInInstitute(UserInfoDto userInfoDto) {
        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<DiseaseInformation> recoverTodayDiseasesByInstitute = diseaseService.getRecoverTodayDiseasesByInstitute(instituteId);

        return String.valueOf(
                recoverTodayDiseasesByInstitute.size()
        );
    }

    private String getCountOfSickTodayInInstitute(UserInfoDto userInfoDto) {
        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<DiseaseInformation> sickTodayDiseasesByInstitute = diseaseService.getSickTodayDiseasesByInstitute(instituteId);

        return String.valueOf(
                sickTodayDiseasesByInstitute.size()
        );
    }

    private CountOfDiseasesByDays getCountOfDiseasesByDaysForTwoWeeks(UserInfoDto userInfoDto) {
        LocalDate startDate = LocalDate.now().minusDays(TWO_WEEKS_DAYS_COUNT - ONE_DAY);
        LocalDate endDate = LocalDate.now();

        String instituteId = getDecanatUserInstituteId(userInfoDto);

        List<DiseaseInformation> notRejectedDiseases = diseaseService.getNotRejectedDiseasesByInstitute(instituteId);

        return graphicsService.getCountOfDiseasesByDays(notRejectedDiseases, startDate, endDate);
    }

    private String getDecanatUserInstituteId(UserInfoDto userInfoDto) {
        return Optional.ofNullable(userInfoDto)
                .map(UserInfoDto::getInstitute)
                .map(InstituteResponse::getId)
                .orElse(StringUtils.EMPTY);
    }

    private void sendNotificationAboutDiseaseApprove(DiseaseInformation diseaseInformation) {
        String userId = getUserId(diseaseInformation);
        User user = getUserById(userId);
        mailSender.sendDiseaseApprovedMessage(user);
    }

    private void sendNotificationAboutDiseaseRefund(DiseaseInformation diseaseInformation, String refundCause) {
        String userId = getUserId(diseaseInformation);
        User user = getUserById(userId);

        mailSender.sendDiseaseRefundToUserMessage(user, refundCause);
    }

    private void sendNotificationAboutDiseaseReject(DiseaseInformation diseaseInformation, String rejectCause) {
        String userId = getUserId(diseaseInformation);
        User user = getUserById(userId);

        mailSender.sendDiseaseRejectMessage(user, rejectCause);
    }

    private String getDecanatInstituteId(User decanatUser) {
        return Optional.ofNullable(decanatUser)
                .map(User::getInstitute)
                .map(Institute::getId)
                .orElse(StringUtils.EMPTY);
    }

    private String getUserId(DiseaseInformation diseaseInformation) {
        return Optional.ofNullable(diseaseInformation)
                .map(DiseaseInformation::getUser)
                .map(User::getId)
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
