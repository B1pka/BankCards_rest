package com.example.bankcards.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @GetMapping("/api/debug/me")
    public String me(Authentication authentication) {
        if (authentication == null) {
            return "authentication is null";
        }

        return "name=" + authentication.getName()
                + ", authorities=" + authentication.getAuthorities()
                + ", authenticated=" + authentication.isAuthenticated();
    }
}