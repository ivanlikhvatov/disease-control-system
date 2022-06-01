package ru.example.dto.response;

import lombok.Data;
import ru.example.dao.entity.user.Role;
import ru.example.dao.entity.Status;
import ru.example.dto.response.decanatAdditionalInfo.DecanatAdditionalInfoResponse;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserInfoDto {
    private String login;

    private String email;

    private String phoneNumber;

    private String firstname;

    private String lastname;

    private String patronymic;

    private GroupResponse group;

    private LocalDateTime updated;

    private LocalDateTime created;

    private Status status;

    private Set<Role> roles;

    //TODO перенести это в decanatAdditionalInfo
    private InstituteResponse institute;

    private DecanatAdditionalInfoResponse decanatAdditionalInfo;
}
