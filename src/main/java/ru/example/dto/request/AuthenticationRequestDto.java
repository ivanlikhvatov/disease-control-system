package ru.example.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthenticationRequestDto {

    @NotBlank
    @Size(max = 20)
    private String login;

    @NotBlank
    @Size(max = 30)
    private String password;
}
