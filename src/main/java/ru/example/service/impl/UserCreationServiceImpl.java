package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.creationService.UserCreationRequestDto;
import ru.example.dto.response.StatusResult;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.UserCreationRequestDtoMapper;
import ru.example.repository.UserRepository;
import ru.example.service.UserCreationService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCreationServiceImpl implements UserCreationService {

    private final UserCreationRequestDtoMapper mapper;
    private final UserRepository userRepository;


    @Override
    public StatusResult createUser(UserCreationRequestDto request) {
        validateRequest(request);

        User user = mapper.map(request);
        userRepository.save(user);

        return StatusResult.ok();
    }

    private void validateRequest(UserCreationRequestDto request) {
        Optional.ofNullable(userRepository.findByLogin(request.getLogin()))
                .ifPresent(user -> {
                    throw new ApiException(ErrorContainer.USER_WITH_THIS_LOGIN_EXIST);
                });
    }
}
