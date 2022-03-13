package ru.example.service;

import ru.example.dto.request.AuthenticationRequestDto;
import ru.example.dto.response.LoginResponseDto;

public interface AuthenticationService {
    LoginResponseDto login(AuthenticationRequestDto request);
}
