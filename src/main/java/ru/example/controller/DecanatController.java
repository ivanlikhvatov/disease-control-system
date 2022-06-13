package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.StatusResult;
import ru.example.security.jwt.JwtUser;
import ru.example.service.DecanatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/decanat")
public class DecanatController {

    private final DecanatService decanatService;

    @GetMapping("/diseases/processed")
    public List<DiseaseInfoResponse> getProcessedDiseases(@AuthenticationPrincipal JwtUser jwtUser) {
        return decanatService.getProcessedDiseasesByInsitute(jwtUser);
    }

    @GetMapping("/diseases/active")
    public List<DiseaseInfoResponse> getActiveDiseases(@AuthenticationPrincipal JwtUser jwtUser) {
        return decanatService.getActiveDiseasesByInstitute(jwtUser);
    }

    @GetMapping("/diseases/all")
    public List<DiseaseInfoResponse> getAllDiseases(@AuthenticationPrincipal JwtUser jwtUser) {
        return decanatService.getAllDiseaseInformationByInstitute(jwtUser);
    }

    @PostMapping("/diseases/{diseaseId}/approve")
    public StatusResult approveDiseaseByDecanat(@PathVariable String diseaseId, @AuthenticationPrincipal JwtUser jwtUser) {
        return decanatService.approveDiseaseByDecanat(diseaseId, jwtUser);
    }

    @PostMapping("/diseases/{diseaseId}/refund")
    public StatusResult refundDiseaseToStudent(@PathVariable String diseaseId, @RequestParam String refundCause) {
        return decanatService.refundDiseaseToStudent(diseaseId, refundCause);
    }

    @PostMapping("/diseases/{diseaseId}/reject")
    public StatusResult rejectDisease(@PathVariable String diseaseId, @RequestParam String rejectCause, @AuthenticationPrincipal JwtUser jwtUser) {
        return decanatService.rejectDisease(diseaseId, rejectCause, jwtUser);
    }
}
