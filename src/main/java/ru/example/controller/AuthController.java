package ru.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
    public LoginResponseDto loginUser(@RequestBody @Valid AuthenticationRequestDto request) {
        return authenticationService.loginUser(request);
    }

    @PostMapping("/registration")
    public StatusResult registerUser(@RequestBody @Valid RegistrationRequestDto request) {
        return registrationService.registerUser(request);
    }

    @PostMapping("/code/resend")
    public StatusResult resendActivationCode(@RequestParam String expiredActivationCode) {
        return registrationService.resendActivationCode(expiredActivationCode);
    }

    @PostMapping("/activate")
    public StatusResult activateUser(@RequestParam String activationCode) {
        return registrationService.activateUser(activationCode);
    }



}
