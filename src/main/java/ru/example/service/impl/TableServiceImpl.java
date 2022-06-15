package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.user.Role;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.security.jwt.JwtUser;
import ru.example.service.CuratorAndTeacherService;
import ru.example.service.DecanatService;
import ru.example.service.RectoratService;
import ru.example.service.TableService;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final DecanatService decanatService;
    private final RectoratService rectoratService;
    private final CuratorAndTeacherService curatorAndTeacherService;

    @Override
    public List<DiseaseInfoResponse> getActiveDiseases(JwtUser jwtUser) {

        if (jwtUser.getRoles().contains(Role.DECANAT)) {
            return decanatService.getActiveDiseasesByInstitute(jwtUser);
        }

        if (jwtUser.getRoles().contains(Role.RECTORAT)) {
            return rectoratService.getActiveDiseasesByUniversity(jwtUser);
        }

        if (jwtUser.getRoles().contains(Role.CURATOR) || jwtUser.getRoles().contains(Role.TEACHER)) {
            return curatorAndTeacherService.getActiveDiseasesByInterestedGroups(jwtUser);
        }

        return Collections.emptyList();
    }

    @Override
    public List<DiseaseInfoResponse> getAllDiseaseInformation(JwtUser jwtUser) {

        if (jwtUser.getRoles().contains(Role.DECANAT)) {
            return decanatService.getAllDiseaseInformationByInstitute(jwtUser);
        }

        if (jwtUser.getRoles().contains(Role.RECTORAT)) {
            return rectoratService.getAllDiseaseInformationByUniversity(jwtUser);
        }

        if (jwtUser.getRoles().contains(Role.CURATOR) || jwtUser.getRoles().contains(Role.TEACHER)) {
            return curatorAndTeacherService.getAllDiseasesInformationByInterestedGroups(jwtUser);
        }

        return Collections.emptyList();
    }


}
