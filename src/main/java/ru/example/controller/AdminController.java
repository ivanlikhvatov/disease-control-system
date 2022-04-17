package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.request.creationService.UserCreationRequestDto;
import ru.example.dto.response.StatusResult;
import ru.example.dto.response.UserInfoDto;
import ru.example.service.UserCreationService;
import ru.example.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;
    private final UserCreationService creationService;

    @GetMapping("/users/{studentNumber}")
    public UserInfoDto getUserByStudentNumber(@PathVariable String studentNumber) {
        return userService.getUserInfoDtoByStudentNumber(studentNumber);
    }

    @PostMapping("/user/create")
    public StatusResult createUser(@RequestBody UserCreationRequestDto request) {
        return creationService.createUser(request);
    }


}
