package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.dto.response.UserInfoDto;
import ru.example.security.jwt.JwtUser;
import ru.example.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public UserInfoDto getUserInfo(@AuthenticationPrincipal JwtUser jwtUser) {
        return userService.getUserInfoDtoByLogin(jwtUser.getLogin());
    }
}
