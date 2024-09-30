package com.api.reservavuelos.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;
     @Value("${spring.mail.port}")
    private String port;
     @Value("${spring.mail.username}")
    private String username;
     @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender emailSender() {
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost(host);
        emailSender.setPort(Integer.parseInt(port));
        emailSender.setUsername(username);
        emailSender.setPassword(password);

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
         return emailSender;
    }

}
