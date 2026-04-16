package com.example.bankcards.dto.Card;

import java.math.BigDecimal;

public record CardBalanceResponse(
        Long cardId,
        BigDecimal balance
) {
}
