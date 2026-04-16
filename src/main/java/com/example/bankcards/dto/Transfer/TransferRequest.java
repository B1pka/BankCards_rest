package com.example.bankcards.dto.Transfer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(

        @NotNull
        Long fromCardId,

        @NotNull
        Long toCardId,

        @NotNull
        @Positive
        BigDecimal amount
) {
}
