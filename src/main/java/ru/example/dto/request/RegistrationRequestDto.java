package ru.example.dto.request;

import lombok.Data;
import ru.example.dao.entity.user.Gender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegistrationRequestDto {
    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    private String patronymic;

    @NotBlank
    private String studentNumber;

    @NotNull
    private Gender gender;

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
