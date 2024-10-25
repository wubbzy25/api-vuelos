package com.api.reservavuelos.Services;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthenticatorService {
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateSecretKey(){
    GoogleAuthenticatorKey key = gAuth.createCredentials();
    return key.getKey();
    }

    public boolean validateCode(String secretKey, int code){
     return gAuth.authorize(secretKey, code);
    }

}
