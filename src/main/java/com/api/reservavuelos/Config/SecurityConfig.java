package com.api.reservavuelos.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity()
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity Http) throws Exception {
        Http.authorizeHttpRequests(request -> request
                .requestMatchers("api/v1/auth").permitAll()
                .requestMatchers("api/v1/**").authenticated())
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
                return Http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    UserDetailsService usuarioPrueba(PasswordEncoder passwordEncoder) {
        User.UserBuilder user = User.builder();
        UserDetails usuario = user
                .username("Carlos")
                .password(passwordEncoder.encode("1234"))
                .roles()
                .build();
        return new InMemoryUserDetailsManager(usuario);
    }

}
