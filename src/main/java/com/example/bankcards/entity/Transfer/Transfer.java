package com.example.bankcards.entity.Transfer;

import com.example.bankcards.entity.Card.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Transfer {

    public Transfer() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfers_seq_gen")
    @SequenceGenerator(
            name = "transfers_seq_gen",
            sequenceName = "transfers_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_card_id", nullable = false)
    private Card fromCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_card_id", nullable = false)
    private Card toCard;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
