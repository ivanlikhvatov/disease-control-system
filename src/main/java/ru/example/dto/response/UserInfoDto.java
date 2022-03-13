package ru.example.dto.response;

import lombok.Data;
import ru.example.domain.Role;
import ru.example.domain.Status;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserInfoDto {
    private String studentNumber;

    private String email;

    private String firstname;

    private String lastname;

    private String patronymic;

    private LocalDateTime updated;

    private LocalDateTime created;

    private Status status;

    private Set<Role> roles;
}
