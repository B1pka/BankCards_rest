package com.example.bankcards.dto.User;

import Role;

public record UserResponse(
        Long id,
        String username,
        Role role
) {
}
