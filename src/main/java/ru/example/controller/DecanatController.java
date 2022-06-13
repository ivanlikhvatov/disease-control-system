package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.StatusResult;
import ru.example.security.jwt.JwtUser;
import ru.example.service.DiseaseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/decanat")
public class DecanatController {

    private final DiseaseService diseaseService;

    @GetMapping("/diseases/processed")
    public List<DiseaseInfoResponse> getProcessedDiseases(@AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.getProcessedDiseases(jwtUser);
    }

    @GetMapping("/diseases/active")
    public List<DiseaseInfoResponse> getActiveDiseases(@AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.getActiveDiseases(jwtUser);
    }

    @GetMapping("/diseases/all")
    public List<DiseaseInfoResponse> getAllDiseases(@AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.getAllDiseaseInformationByInstitute(jwtUser);
    }

    @PostMapping("/diseases/{diseaseId}/approve")
    public StatusResult approveDiseaseByDecanat(@PathVariable String diseaseId, @AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.approveDiseaseByDecanat(diseaseId, jwtUser);
    }

    @PostMapping("/diseases/{diseaseId}/refund")
    public StatusResult refundDiseaseToStudent(@PathVariable String diseaseId, @RequestParam String refundCause) {
        return diseaseService.refundDiseaseToStudent(diseaseId, refundCause);
    }

    @PostMapping("/diseases/{diseaseId}/reject")
    public StatusResult rejectDisease(@PathVariable String diseaseId, @RequestParam String rejectCause, @AuthenticationPrincipal JwtUser jwtUser) {
        return diseaseService.rejectDisease(diseaseId, rejectCause, jwtUser);
    }
}
