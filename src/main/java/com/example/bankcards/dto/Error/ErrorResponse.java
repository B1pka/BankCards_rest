package com.example.bankcards.dto.Error;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime errortime,
        int status,
        String error,
        String message,
        String path
) {
}
