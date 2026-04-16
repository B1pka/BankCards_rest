package com.example.bankcards.repository.Card;

import com.example.bankcards.entity.Card.Card;
import com.example.bankcards.entity.Card.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Page<Card> findAllByUserId(Long userId, Pageable pageable);

    Optional<Card> findByIdAndUserId(Long cardId, Long userId);

    Page<Card> findAllByUserIdAndStatus(Long userId, CardStatus status, Pageable pageable);
}
