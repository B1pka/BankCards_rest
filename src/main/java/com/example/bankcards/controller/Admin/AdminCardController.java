package com.example.bankcards.controller.Admin;

import com.example.bankcards.dto.Card.CardResponse;
import com.example.bankcards.dto.Card.CreateCardRequest;
import com.example.bankcards.service.Card.CardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cards")
public class AdminCardController {

    private final CardService cardService;

    public AdminCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public CardResponse createCard(@RequestBody @Valid CreateCardRequest request){
        System.out.println(">>> ADMIN CREATED CARD: " + request);
        return cardService.createCard(request);
    }

    @GetMapping
    public Page<CardResponse> getAllCards(Pageable pageable){
        return cardService.getAllCards(pageable);
    }

    @PatchMapping("/{id}/block")
    public CardResponse blockCard(@PathVariable Long id){
        return cardService.blockCard(id);
    }

    @PatchMapping("/{id}")
    public void deleteCard(@PathVariable Long id){
        cardService.deleteCard(id);
    }

    @PatchMapping("/{id}/activate")
    public CardResponse activeCard(@PathVariable Long id){
        return cardService.activateCard(id);
    }
}
