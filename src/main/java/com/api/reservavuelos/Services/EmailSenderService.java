package com.api.reservavuelos.Services;

import com.api.reservavuelos.Exceptions.EmailDonSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendRestPasswordEmail(String to, String from, String email){

    }

    public void sendVerifyEmail(String to, String from, String email){

    }

    @Async
    public void sendEmail(String to, String from, String email){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setText(email);
        } catch(MessagingException e){
              throw new EmailDonSendException();
        }
    }
}
