package com.example.bankcards.repository.Transfer;

import com.example.bankcards.entity.Transfer.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
