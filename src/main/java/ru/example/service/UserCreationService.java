package ru.example.service;

import ru.example.dto.request.creationService.UserCreationRequestDto;
import ru.example.dto.response.StatusResult;

public interface UserCreationService {
    StatusResult createUser(UserCreationRequestDto request);
}
