package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.dto.request.AuthenticationRequestDto;
import ru.example.dto.request.RegistrationRequestDto;
import ru.example.dto.response.LoginResponseDto;
import ru.example.dto.response.StatusResult;
import ru.example.service.AuthenticationService;
import ru.example.service.RegistrationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RegistrationService registrationService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid AuthenticationRequestDto request) {
        return authenticationService.login(request);
    }

    @PostMapping("/registration")
    public StatusResult registration(@RequestBody @Valid RegistrationRequestDto request) {
        return registrationService.register(request);
    }


}
