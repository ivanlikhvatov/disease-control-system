package ru.example.service;

import ru.example.dao.entity.user.User;
import ru.example.dto.response.UserAdditionalInfo;
import ru.example.dto.response.UserInfoDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    UserInfoDto getUserInfoDtoByLogin(String login);

    User getUserByLogin(String login);

    User getUserById(String id);

    UserAdditionalInfo getUserAdditionalInfo(UserInfoDto userInfoDto);
}
