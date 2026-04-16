package com.example.bankcards.service.Card;

import org.springframework.stereotype.Service;

@Service
public class CardMaskingService {

    String mask(String last4){
        return "**** **** **** " + last4;
    }
}
