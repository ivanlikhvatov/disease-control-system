package ru.example.dto.request.creationService;

import lombok.Data;
import ru.example.dao.entity.user.Gender;
import ru.example.dao.entity.user.Role;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UserCreationRequestDto {
    @NotBlank
    @Size(max = 20)
    private String studentNumber;

    @NotBlank
    @Size(max = 20)
    private String firstname;

    @NotBlank
    @Size(max = 20)
    private String lastname;

    @Size(max = 20)
    private String patronymic;

    @NotNull
    private Gender gender;

    @NotNull
    @Valid
    private GroupRequest group;

    @NotNull
    private Set<Role> roles;
}
