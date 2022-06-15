package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.dto.response.DiseaseInfoResponse;
import ru.example.security.jwt.JwtUser;
import ru.example.service.TableService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tables")
public class TableController {

    private final TableService tableService;

    @GetMapping("/diseases/active")
    public List<DiseaseInfoResponse> getActiveDiseases(@AuthenticationPrincipal JwtUser jwtUser) {
        return tableService.getActiveDiseases(jwtUser);
    }

    @GetMapping("/diseases/all")
    public List<DiseaseInfoResponse> getAllDiseases(@AuthenticationPrincipal JwtUser jwtUser) {
        return tableService.getAllDiseaseInformation(jwtUser);
    }
}
