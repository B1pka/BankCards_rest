package com.example.bankcards.exception.CardException;

public class CardNotFoundException extends RuntimeException{
    public CardNotFoundException(String message) { super(message); }
}
