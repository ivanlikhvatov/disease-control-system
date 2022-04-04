package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dto.response.LoginResponseDto;
import ru.example.dto.response.UserInfoDto;

@Mapper
public interface LoginResponseDtoMapper {
    LoginResponseDto map(UserInfoDto userInfoDto, String token);
}
