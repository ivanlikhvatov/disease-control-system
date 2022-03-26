package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.Status;
import ru.example.dao.entity.user.User;
import ru.example.dto.request.RegistrationRequestDto;
import ru.example.dto.response.StatusResult;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.RegistrationRequestDtoMapper;
import ru.example.repository.UserRepository;
import ru.example.service.MailSender;
import ru.example.service.RegistrationService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final RegistrationRequestDtoMapper mapper;
    private final MailSender mailSender;
    private final UserRepository userRepository;

    @Override
    public StatusResult registerUser(RegistrationRequestDto request) {
        checkUser(request);

        encodePassword(request);
        User user = mapper.map(request);

        mailSender.sendConfirmationMessage(user);
        userRepository.save(user);

        return StatusResult.ok();
    }

    @Override
    public StatusResult activateUser(String activationCode) {
        User user = userRepository.findByActivationCode(activationCode);

        checkUserActivationCode(user);
        activateUserEntity(user);

        mailSender.sendSuccessActivationMessage(user);
        userRepository.save(user);

        return StatusResult.ok();
    }

    @Override
    public StatusResult resendActivationCode(String expiredActivationCode) {

        User user = userRepository.findByActivationCode(expiredActivationCode);
        checkUserExpiredActivationCode(user);

        String newActivationCode = UUID.randomUUID().toString();
        user.setActivationCode(newActivationCode);

        mailSender.sendConfirmationMessage(user);
        userRepository.save(user);

        return StatusResult.ok();
    }

    private void checkUserExpiredActivationCode(User user) {
        if (user == null) {
            throw new ApiException(ErrorContainer.USER_ALREADY_CONFIRM_EMAIL);
        }

        if (user.getActivationCodeExpirationDate().isAfter(LocalDateTime.now())) {
            throw new ApiException(ErrorContainer.OTHER);
        }
    }

    private void checkUserActivationCode(User user) {
        if (user == null) {
            throw new ApiException(ErrorContainer.ACTIVATION_CODE_NOT_FOUND);
        }

        if (user.getActivationCodeExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ApiException(ErrorContainer.ACTIVATION_CODE_EXPIRED);
        }
    }


    private void checkUser(RegistrationRequestDto request) {
        Optional.ofNullable(userRepository.findByStudentNumber(request.getStudentNumber()))
                .ifPresent(user -> {
                    throw new ApiException(ErrorContainer.USER_WITH_THIS_STUDENT_NUMBER_EXIST);
                });

        Optional.ofNullable(userRepository.findByEmail(request.getEmail()))
                .ifPresent(user -> {
                    throw new ApiException(ErrorContainer.USER_WITH_THIS_EMAIL_EXIST);
                });
    }

    private void encodePassword(RegistrationRequestDto request) {
        String decodePassword = request.getPassword();
        String encodePassword = passwordEncoder.encode(decodePassword);
        request.setPassword(encodePassword);
    }

    private void activateUserEntity(User user) {
        user.setActivationCode(null);
        user.setActivationCodeExpirationDate(null);
        user.setStatus(Status.ACTIVE);
    }
}
