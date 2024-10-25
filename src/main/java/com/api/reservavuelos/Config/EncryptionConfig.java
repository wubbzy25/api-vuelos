package com.api.reservavuelos.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class EncryptionConfig {

    @Value("${encryption.secret-key}")
    private String secretKeyBase64;

    @Bean
    public SecretKey secretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyBase64);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}

