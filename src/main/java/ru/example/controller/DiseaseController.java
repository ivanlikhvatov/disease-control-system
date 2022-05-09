package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.request.disease.DiseaseInformationRequest;
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

    @GetMapping
    public List<DiseaseResponse> getDiseases() {
        return diseaseService.getDiseases();
    }

    @GetMapping("/info")
    public DiseaseInfoResponse getNotClosedDisease(@AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.getNotClosedDisease(jwtUser);
    }

    @PostMapping("/info/add")
    public StatusResult addDiseaseInfo(@RequestBody @Valid DiseaseInformationRequest request,
                                       @AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.addDiseaseInfo(request, jwtUser);
    }


}
