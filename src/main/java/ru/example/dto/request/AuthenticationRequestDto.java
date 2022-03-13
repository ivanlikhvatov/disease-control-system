package ru.example.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationRequestDto {

    @NotBlank
    private String studentNumber;

    @NotBlank
    private String password;
}
