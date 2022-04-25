package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.dto.response.DiseaseResponse;
import ru.example.service.DiseaseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student/disease")
public class DiseaseController {

    private final DiseaseService diseaseService;

    @GetMapping
    public List<DiseaseResponse> getDiseases() {
        return diseaseService.getDiseases();
    }
}
