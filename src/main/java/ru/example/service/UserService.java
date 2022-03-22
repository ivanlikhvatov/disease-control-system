package ru.example.service;

import ru.example.domain.User;
import ru.example.dto.response.UserInfoDto;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    UserInfoDto getUserInfoDtoByStudentNumber(String studentNumber);

    User getUserByStudentNumber(String studentNumber);

    User getById(Long id);
}
