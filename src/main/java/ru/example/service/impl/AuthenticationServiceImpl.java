package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.Status;
import ru.example.dto.request.AuthenticationRequestDto;
import ru.example.dto.response.LoginResponseDto;
import ru.example.dto.response.UserInfoDto;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.LoginResponseDtoMapper;
import ru.example.security.jwt.JwtTokenProvider;
import ru.example.service.AuthenticationService;
import ru.example.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final LoginResponseDtoMapper mapper;


    @Override
    public LoginResponseDto loginUser(AuthenticationRequestDto request) {
        String studentNumber = request.getStudentNumber();
        String password = request.getPassword();

        UserInfoDto user = userService.getUserInfoDtoByStudentNumber(studentNumber);

        checkUser(user);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(studentNumber, password);

        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenProvider.createToken(request.getStudentNumber(), user.getRoles());

        return mapper.map(user, token);
    }

    private void checkUser(UserInfoDto user) {
        if (!Status.ACTIVE.equals(user.getStatus())){
            throw new ApiException(ErrorContainer.USER_STATUS_NOT_ACTIVE);
        }
    }
}
