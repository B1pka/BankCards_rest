package com.example.bankcards.service.Transfer;

import com.example.bankcards.dto.Transfer.TransferRequest;
import com.example.bankcards.dto.Transfer.TransferResponse;
import com.example.bankcards.entity.Card.Card;
import com.example.bankcards.entity.Card.CardStatus;
import com.example.bankcards.entity.Transfer.Transfer;
import com.example.bankcards.exception.CardException.CardBlockedException;
import com.example.bankcards.exception.CardException.CardExpiredException;
import com.example.bankcards.exception.CardException.CardNotFoundException;
import com.example.bankcards.exception.TransferExceptions.TransferNotAllowedException;
import com.example.bankcards.exception.UserExceptions.InsufficientFundsException;
import com.example.bankcards.repository.Card.CardRepository;
import com.example.bankcards.repository.Transfer.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferService {

    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;

    @Transactional
    public TransferResponse transferBetweenOwnCards(Long userId, TransferRequest request) {
        validateTransferRequest(request);

        Card fromCard = cardRepository.findByIdAndUserId(request.fromCardId(), userId)
                .orElseThrow(() -> new CardNotFoundException(
                        "Source card not found"
                ));

        Card toCard = cardRepository.findByIdAndUserId(request.toCardId(), userId)
                .orElseThrow(() -> new CardNotFoundException(
                        "Target card not found"
                ));

        validateCardAvailableForTransfer(fromCard);
        validateCardAvailableForTransfer(toCard);

        if (fromCard.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(request.amount()));
        toCard.setBalance(toCard.getBalance().add(request.amount()));

        Transfer transfer = Transfer.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(request.amount())
                .createdAt(LocalDateTime.now())
                .build();

        Transfer savedTransfer = transferRepository.save(transfer);

        return new TransferResponse(
                savedTransfer.getId(),
                savedTransfer.getFromCard().getId(),
                savedTransfer.getToCard().getId(),
                savedTransfer.getAmount(),
                savedTransfer.getCreatedAt()
        );
    }

    private void validateTransferRequest(TransferRequest request) {
        if (request.fromCardId().equals(request.toCardId())) {
            throw new TransferNotAllowedException("Operation cannot be completed");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferNotAllowedException("Enter the correct amount");
        }
    }

    private void validateCardAvailableForTransfer(Card card) {
        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new CardBlockedException("Card is blocked");
        }

        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            throw new CardExpiredException("Card is expired");
        }
    }
}