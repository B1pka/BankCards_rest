package com.example.bankcards.controller.User;

import com.example.bankcards.dto.Card.CardBalanceResponse;
import com.example.bankcards.dto.Card.CardResponse;
import com.example.bankcards.entity.User.User;
import com.example.bankcards.service.Card.CardService;
import com.example.bankcards.service.User.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards/my")
public class UserCardCotroller {

    private final CardService cardService;
    private final UserService userService;

    public UserCardCotroller(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @GetMapping
    public Page<CardResponse> getMyCards(Authentication authentication, Pageable pageable){
        User currentUser = userService.getUserEntityByUsername(authentication.getName());
        return cardService.getUserCards(currentUser.getId(), pageable);
    }

    @GetMapping("/{id}")
    public CardResponse getMyCardById(@PathVariable Long id, Authentication authentication){
        User currentUser = userService.getUserEntityByUsername(authentication.getName());
        return cardService.getUserCardById(id, currentUser.getId());
    }

    @GetMapping("/{id}/balance")
    public CardBalanceResponse getMyCads(@PathVariable Long id, Authentication authentication){
        User currentUser = userService.getUserEntityByUsername(authentication.getName());
        return cardService.getUserCardBalance(id, currentUser.getId());
    }

    @PatchMapping("/{id}/block_request")
    public CardResponse blockRequest(@PathVariable Long id, Authentication authentication){
        User currentUser = userService.getUserEntityByUsername(authentication.getName());
        return cardService.requestBlock(id, currentUser.getId());
    }
}
