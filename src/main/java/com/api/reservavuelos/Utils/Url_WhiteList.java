package com.api.reservavuelos.Utils;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Url_WhiteList {

    @Bean
    public List<String> Url_whiteList() {
        return Arrays.asList(
                "/api/v1/auth/register",
                "/api/v1/auth/login",
                "/api/v1/auth/forgot-password",
                "/api/v1/auth/verify-code",
                "/api/v1/auth/change-password",
                "/api/v1/auth/2FA/setup",
                "/api/v1/auth/2FA/verify"
        );
    }
}
