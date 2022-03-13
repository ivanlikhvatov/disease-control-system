package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.response.UserInfoDto;
import ru.example.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public UserInfoDto getUserByStudentNumber(@RequestParam String studentNumber) {
        return userService.getUserInfoDtoByStudentNumber(studentNumber);
    }


}
