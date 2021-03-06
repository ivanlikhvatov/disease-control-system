package ru.example.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequestDto {
    @NotBlank
    @Size(max = 20)
    private String login;

    @NotBlank
    @Email
    @Size(max = 30)
    private String email;

    @NotBlank
    @Size(max = 10)
    @Size(min = 10) //TODO Валидация на номер телефона
    private String phoneNumber;

    @NotBlank
    @Size(max = 30)
    private String password;
}
