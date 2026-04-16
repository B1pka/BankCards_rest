package com.example.bankcards.service.Card;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EncryptionService {

    private SecretKey secretKey;

    @Value("${app.encryption.secret}")
    private String secret;

    @PostConstruct
    public void init(){
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        if(keyBytes.length != 16){
            throw new IllegalStateException("Неверная длина шифровального ключа");
        }
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String rawCardNumber){
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(rawCardNumber.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e){
            throw new IllegalStateException("Ошибка шифрования карты");
        }
    }

    public String decrypt(String encryptedCardNumber){
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encryptedCardNumber);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e){
            throw new IllegalStateException("Ошибка дешиврования карты");
        }
    }
}
