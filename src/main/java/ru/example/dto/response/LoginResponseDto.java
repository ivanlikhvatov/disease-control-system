package ru.example.dto.response;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String studentNumber;
    private String token;
}
