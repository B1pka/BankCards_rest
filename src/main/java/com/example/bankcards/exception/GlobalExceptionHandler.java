package com.example.bankcards.exception;

import com.example.bankcards.dto.Error.ErrorResponse;
import com.example.bankcards.exception.CardException.CardBlockedException;
import com.example.bankcards.exception.CardException.CardExpiredException;
import com.example.bankcards.exception.CardException.CardNotFoundException;
import com.example.bankcards.exception.TransferExceptions.TransferNotAllowedException;
import com.example.bankcards.exception.UserExceptions.InsufficientFundsException;
import com.example.bankcards.exception.UserExceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CardNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> hadleEntityNotFound(
            RuntimeException exception,
            HttpServletRequest request
    ){
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler({
            InsufficientFundsException.class,
            CardBlockedException.class,
            CardExpiredException.class,
            TransferNotAllowedException.class,
            IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(
            RuntimeException exception,
            HttpServletRequest request
    ){
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(
            Exception exception, HttpServletRequest request
    ){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ){
        String message = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, String message,
            HttpServletRequest request
    ){
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }
}
