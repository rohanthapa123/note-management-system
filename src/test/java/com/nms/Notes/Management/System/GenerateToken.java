package com.nms.Notes.Management.System;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class GenerateToken {

    @Test
    public void JwtServices() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secret = keyGenerator.generateKey();
            String finalkey = Base64.getEncoder().encodeToString(secret.getEncoded());
            System.out.println(finalkey);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
    }
}
