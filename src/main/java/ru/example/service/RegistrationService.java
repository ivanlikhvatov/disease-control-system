package ru.example.service;

import ru.example.dto.request.RegistrationRequestDto;
import ru.example.dto.response.StatusResult;

public interface RegistrationService {
    StatusResult registerUser(RegistrationRequestDto request);

    StatusResult activateUser(String activationCode);

    StatusResult resendActivationCode(String expiredActivationCode);
}
