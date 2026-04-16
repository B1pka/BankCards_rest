package com.example.bankcards.controller.Transfer;

import com.example.bankcards.dto.Transfer.TransferRequest;
import com.example.bankcards.dto.Transfer.TransferResponse;
import com.example.bankcards.entity.User.User;
import com.example.bankcards.service.Transfer.TransferService;
import com.example.bankcards.service.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final UserService userService;

    @PostMapping
    public TransferResponse transfer(@RequestBody @Valid TransferRequest request, Authentication authentication){
        System.out.println(">>> TRANSFER CONTROLLER: " + request);
        User currentUser = userService.getUserEntityByUsername(authentication.getName());
        return transferService.transferBetweenOwnCards(currentUser.getId(), request);
    }
}
