package com.example.bankcards.dto.Card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCardRequest(
        @NotNull
        Long userId,

        @NotBlank
        @Size(min = 16, max = 19)
        String cardNumber,

        @NotBlank
        String ownerName,

        @NotNull
        LocalDate expirationDate,

        @NotNull
        @PositiveOrZero
        BigDecimal initialBalance
) {
}
