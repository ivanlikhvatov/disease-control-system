package ru.example.dto.request;

import lombok.Data;
import ru.example.dao.entity.user.Gender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequestDto {
    @NotBlank
    @Size(max = 20)
    private String firstname;

    @NotBlank
    @Size(max = 20)
    private String lastname;

    @Size(max = 20)
    private String patronymic;

    @NotBlank
    @Size(max = 20)
    private String studentNumber;

    @NotNull
    private Gender gender;

    @NotBlank
    @Email
    @Size(max = 30)
    private String email;

    @NotBlank
    @Size(max = 30)
    private String password;
}
