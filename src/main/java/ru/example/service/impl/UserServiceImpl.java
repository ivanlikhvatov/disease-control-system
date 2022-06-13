package ru.example.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.dao.entity.user.Role;
import ru.example.dao.entity.user.User;
import ru.example.dto.response.UserAdditionalInfo;
import ru.example.dto.response.UserInfoDto;
import ru.example.dto.response.decanatAdditionalInfo.DecanatAdditionalInfo;
import ru.example.error.ApiException;
import ru.example.error.ErrorContainer;
import ru.example.mapper.UserInfoResponseDtoMapper;
import ru.example.repository.DepartmentRepository;
import ru.example.repository.UserRepository;
import ru.example.service.DecanatService;
import ru.example.service.DiseaseService;
import ru.example.service.UserService;

import java.util.*;

@Data
@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final DecanatService decanatService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserInfoResponseDtoMapper mapper;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserInfoDto getUserInfoDtoByLogin(String login) {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new ApiException(ErrorContainer.USER_NOT_FOUND);
        }

        UserInfoDto userInfoDto = mapper.map(user);
        UserAdditionalInfo userAdditionalInfo = getUserAdditionalInfo(userInfoDto);

        userInfoDto.setAdditionalInfo(userAdditionalInfo);


        return userInfoDto;
    }

    @Override
    public UserAdditionalInfo getUserAdditionalInfo(UserInfoDto userInfoDto) {

        UserAdditionalInfo userAdditionalInfo = new UserAdditionalInfo();

        if (userInfoDto.getRoles().contains(Role.DECANAT)) {
            DecanatAdditionalInfo decanatAdditionalInfo = decanatService.buildDecanatAdditionalInfo(userInfoDto);
            userAdditionalInfo.setDecanatAdditionalInfo(decanatAdditionalInfo);
        }

        return userAdditionalInfo;
    }

    @Override
    public User getUserByLogin(String login) {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new ApiException(ErrorContainer.USER_NOT_FOUND);
        }

        return user;
    }

    @Override
    public User getUserById(String id) {
        return Optional.of(userRepository.findById(id))
                .get()
                .orElse(null);
    }
}
