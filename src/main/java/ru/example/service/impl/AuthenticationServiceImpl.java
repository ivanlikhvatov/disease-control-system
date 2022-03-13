package ru.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.example.dto.request.AuthenticationRequestDto;
import ru.example.dto.response.LoginResponseDto;
import ru.example.dto.response.UserInfoDto;
import ru.example.security.jwt.JwtTokenProvider;
import ru.example.service.AuthenticationService;
import ru.example.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    @Override
    public LoginResponseDto login(AuthenticationRequestDto request) {
        String studentNumber = request.getStudentNumber();
        String password = request.getPassword();

        UserInfoDto user = userService.getUserInfoDtoByStudentNumber(studentNumber);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(studentNumber, password);

        authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenProvider.createToken(request.getStudentNumber(), user.getRoles());

        return buildLoginResponseDto(token, studentNumber);
    }

    private LoginResponseDto buildLoginResponseDto(String token, String studentNumber) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(token);
        loginResponseDto.setStudentNumber(studentNumber);

        return loginResponseDto;
    }
}
