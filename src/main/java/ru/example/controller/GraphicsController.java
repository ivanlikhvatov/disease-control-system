package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.request.graphics.GroupGraphicRequest;
import ru.example.dto.response.graphics.GroupGraphicInfo;
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
}
