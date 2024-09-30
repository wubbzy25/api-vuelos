package com.api.reservavuelos.Utils;

import com.api.reservavuelos.Filters.URLFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Url_WhiteList {

    @Bean
    public List<String> Url_whiteList() {
        List<String> URL_WHITELIST = Arrays.asList(
                "/api/v1/auth/register",
                "/api/v1/auth/login",
                "/api/v1/auth/forgot-password"
        );
        return URL_WHITELIST;
    }
}
