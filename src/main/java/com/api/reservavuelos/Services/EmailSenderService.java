package com.api.reservavuelos.Services;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Value("${spring.mail.username}")
    private String email;

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }


    @Async
    public void sendRestPasswordEmail(String from, String code){
        String emailContent = "Hola! Te hemos enviado un codigo para restablecer tu contraseña.\n" +
                "Aqui tienes el codigo para reestablecer tu contraseña: " + code + "\n" +
                "Si no has solicitado este cambio, no te preocupes, tu contraseña seguirá siendo segura.\n" +
                "Gracias por utilizar nuestro sistema!";
        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email);
            helper.setTo(from);
            helper.setSubject("Restablecimiento de contraseña");
            helper.setText(emailContent);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendEmailWithQRCode(String from, byte[] qrCodeImage){
        MimeMessage message = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email);
            helper.setTo(from);
            helper.setSubject("Setear 2FA");
            helper.setText("Escanea el siguiente codigo de barra para activar 2FA");
            helper.addAttachment("QrCode2FA.png", new ByteArrayResource(qrCodeImage));
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    }

