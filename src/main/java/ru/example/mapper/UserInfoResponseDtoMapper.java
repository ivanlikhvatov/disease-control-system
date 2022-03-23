package ru.example.mapper;

import org.mapstruct.Mapper;
import ru.example.dao.entity.user.User;
import ru.example.dto.response.UserInfoDto;

@Mapper
public interface UserInfoResponseDtoMapper {
    UserInfoDto map(User user);
}
