package com.example.bankcards.dto.Card;

import com.example.bankcards.entity.Card.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardResponse(
        Long id,
        Long userId,
        String maskedNumber,
        String ownerName,
        CardStatus status,
        BigDecimal balance,
        LocalDate expirationDate
) {
}
