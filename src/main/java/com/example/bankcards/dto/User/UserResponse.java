package com.example.bankcards.dto.User;


import com.example.bankcards.entity.User.Role;

public record UserResponse(
        Long id,
        String username,
        Role role
) {
}
