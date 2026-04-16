package com.example.bankcards.controller.Card;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

    @GetMapping("/api/cards")
    public String cards() {
        return "cards endpoint works";
    }
}