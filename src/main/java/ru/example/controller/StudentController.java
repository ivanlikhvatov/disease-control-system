package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.request.disease.ApproveDiseaseRequest;
import ru.example.dto.request.disease.AddDiseaseInformationRequest;
import ru.example.dto.request.disease.EditDiseaseInformationRequest;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.dto.response.StatusResult;
import ru.example.security.jwt.JwtUser;
import ru.example.service.StudentService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/diseases/info")
    public DiseaseInfoResponse getNotClosedDisease(@AuthenticationPrincipal JwtUser jwtUser) {
        return studentService.getActiveDisease(jwtUser);
    }

    @PostMapping("/diseases/info/add")
    public StatusResult addDiseaseInfo(@RequestBody @Valid AddDiseaseInformationRequest request,
                                       @AuthenticationPrincipal JwtUser jwtUser) {
        return studentService.addDiseaseInfo(request, jwtUser);
    }

    @PostMapping("/diseases/info/edit")
    public StatusResult editDiseaseInfo(@RequestBody @Valid EditDiseaseInformationRequest request,
                                       @AuthenticationPrincipal JwtUser jwtUser) {
        return studentService.editDiseaseInfo(request, jwtUser);
    }

    @PostMapping("/diseases/info/approve/bySick")
    public StatusResult approveDiseaseBySick(@RequestBody @Valid ApproveDiseaseRequest request,
                                       @AuthenticationPrincipal JwtUser jwtUser) {
        return studentService.approveDiseaseBySick(request, jwtUser);
    }


}
