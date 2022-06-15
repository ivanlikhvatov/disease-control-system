package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.request.graphics.DepartmentGraphicRequest;
import ru.example.dto.request.graphics.GroupGraphicRequest;
import ru.example.dto.request.graphics.InstituteGraphicRequest;
import ru.example.dto.request.graphics.UniversityGraphicRequest;
import ru.example.dto.response.graphics.DepartmentGraphicInfo;
import ru.example.dto.response.graphics.GroupGraphicInfo;
import ru.example.dto.response.graphics.InstituteGraphicInfo;
import ru.example.dto.response.graphics.UniversityGraphicInfo;
import ru.example.security.jwt.JwtUser;
import ru.example.service.GraphicsService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/graphics")
public class GraphicsController {

    private final GraphicsService graphicsService;

    @PostMapping("/byGroups")
    public GroupGraphicInfo getGroupGraphicInfo(@RequestBody @Valid GroupGraphicRequest groupGraphicRequest, @AuthenticationPrincipal JwtUser jwtUser) {
        return graphicsService.getGroupGraphicInfo(groupGraphicRequest, jwtUser);
    }

    @PostMapping("/byDepartments")
    public DepartmentGraphicInfo getDepartmentGraphicInfo(@RequestBody @Valid DepartmentGraphicRequest departmentGraphicRequest, @AuthenticationPrincipal JwtUser jwtUser) {
        return graphicsService.getDepartmentGraphicInfo(departmentGraphicRequest, jwtUser);
    }

    @PostMapping("/byInstitutes")
    public InstituteGraphicInfo getInstituteGraphicInfo(@RequestBody @Valid InstituteGraphicRequest instituteGraphicRequest, @AuthenticationPrincipal JwtUser jwtUser) {
        return graphicsService.getInstituteGraphicInfo(instituteGraphicRequest, jwtUser);
    }

    @PostMapping("/byUniversity")
    public UniversityGraphicInfo getUniversityGraphicInfo(@RequestBody @Valid UniversityGraphicRequest universityGraphicRequest, @AuthenticationPrincipal JwtUser jwtUser) {
        return graphicsService.getUniversityGraphicInfo(universityGraphicRequest, jwtUser);
    }

}
