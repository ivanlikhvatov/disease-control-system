package ru.example.dto.response;

import lombok.Data;
import ru.example.dao.entity.user.Role;
import ru.example.dao.entity.Status;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserInfoDto {
    private String login;

    private String email;

    private String firstname;

    private String lastname;

    private String patronymic;

    private GroupResponse group;

    private LocalDateTime updated;

    private LocalDateTime created;

    private Status status;

    private Set<Role> roles;
}
