package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.domain.User;
import ru.example.dto.request.RegistrationRequestDto;
import ru.example.dto.response.StatusResult;
import ru.example.mapper.RegistrationRequestDtoMapper;
import ru.example.service.RegistrationService;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegistrationRequestDtoMapper mapper;

    @Override
    public StatusResult register(RegistrationRequestDto request) {
        String decodePassword = request.getPassword();
        String encodePassword = passwordEncoder.encode(decodePassword);

        request.setPassword(encodePassword);

        User user = mapper.map(request);



        return StatusResult.okResult();
    }
}
