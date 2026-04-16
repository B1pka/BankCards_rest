package com.example.bankcards.dto.Login;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "Введите имя пользователя")
        String username,

        @NotBlank(message = "Введите пароль")
        String password
) {
}
