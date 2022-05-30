package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.request.disease.ApproveDiseaseRequest;
import ru.example.dto.request.disease.AddDiseaseInformationRequest;
import ru.example.dto.request.disease.EditDiseaseInformationRequest;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.DiseaseResponse;
import ru.example.dto.response.StatusResult;
import ru.example.security.jwt.JwtUser;
import ru.example.service.DiseaseService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student/diseases")
public class DiseaseController {

    private final DiseaseService diseaseService;

    @GetMapping//TODO поправить path: student тут лишнее
    public List<DiseaseResponse> getDiseases() {
        return diseaseService.getDiseases();
    }

    @GetMapping("/info")
    public DiseaseInfoResponse getNotClosedDisease(@AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.getActiveDisease(jwtUser);
    }

    @PostMapping("/info/add")
    public StatusResult addDiseaseInfo(@RequestBody @Valid AddDiseaseInformationRequest request,
                                       @AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.addDiseaseInfo(request, jwtUser);
    }

    @PostMapping("/info/edit")
    public StatusResult editDiseaseInfo(@RequestBody @Valid EditDiseaseInformationRequest request,
                                       @AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.editDiseaseInfo(request, jwtUser);
    }

    @PostMapping("/info/approve/bySick")
    public StatusResult approveDiseaseBySick(@RequestBody @Valid ApproveDiseaseRequest request,
                                       @AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.approveDiseaseBySick(request, jwtUser);
    }


}
