package com.example.bankcards.service.Card;

import com.example.bankcards.dto.Card.CardBalanceResponse;
import com.example.bankcards.dto.Card.CardResponse;
import com.example.bankcards.dto.Card.CreateCardRequest;
import com.example.bankcards.entity.Card.Card;
import com.example.bankcards.entity.Card.CardStatus;
import com.example.bankcards.entity.User.User;
import com.example.bankcards.repository.Card.CardRepository;
import com.example.bankcards.repository.User.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final EncryptionService EncryptionService;
    private final CardMaskingService cardMaskingService;

    @Transactional
    public CardResponse createCard(CreateCardRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id %d not found".formatted(request.userId())
                ));

        String normalizedCardNumber = normalizeCardNumber(request.cardNumber());

        Card card = Card.builder()
                .cardNumber(EncryptionService.encrypt(normalizedCardNumber))
                .cardNumberLast4(extractLast4(normalizedCardNumber))
                .ownerName(request.ownerName())
                .expirationDate(request.expirationDate())
                .status(CardStatus.ACTIVE)
                .balance(request.initialBalance())
                .user(user)
                .build();

        Card savedCard = cardRepository.save(card);
        return mapToResponse(savedCard);
    }

    public Page<CardResponse> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public Page<CardResponse> getUserCards(Long userId, Pageable pageable) {
        return cardRepository.findAllByUserId(userId, pageable)
                .map(this::mapToResponse);
    }

    public CardResponse getUserCardById(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Card with id %d not found for user %d".formatted(cardId, userId)
                ));

        return mapToResponse(card);
    }

    public CardBalanceResponse getUserCardBalance(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Card with id %d not found for user %d".formatted(cardId, userId)
                ));

        return new CardBalanceResponse(card.getId(), card.getBalance());
    }

    @Transactional
    public CardResponse requestBlock(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndUserId(cardId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Card with id %d not found for user %d".formatted(cardId, userId)
                ));

        card.setStatus(CardStatus.BLOCK_REQUESTED);
        return mapToResponse(card);
    }

    @Transactional
    public CardResponse blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Card with id %d not found".formatted(cardId)
                ));

        card.setStatus(CardStatus.BLOCKED);
        return mapToResponse(card);
    }

    @Transactional
    public CardResponse activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Card with id %d not found".formatted(cardId)
                ));

        card.setStatus(CardStatus.ACTIVE);
        return mapToResponse(card);
    }

    @Transactional
    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new EntityNotFoundException("Card with id %d not found".formatted(cardId));
        }

        cardRepository.deleteById(cardId);
    }

    private CardResponse mapToResponse(Card card) {
        return new CardResponse(
                card.getId(),
                card.getUser().getId(),
                cardMaskingService.mask(card.getCardNumberLast4()),
                card.getOwnerName(),
                card.getStatus(),
                card.getBalance(),
                card.getExpirationDate()
        );
    }

    private String normalizeCardNumber(String cardNumber) {
        return cardNumber.replaceAll("\\s+", "");
    }

    private String extractLast4(String normalizedCardNumber) {
        return normalizedCardNumber.substring(normalizedCardNumber.length() - 4);
    }
}