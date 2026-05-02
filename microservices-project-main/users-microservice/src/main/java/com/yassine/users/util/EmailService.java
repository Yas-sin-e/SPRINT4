package com.yassine.users.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String emailBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailBody, true);
            helper.setTo(to);
            helper.setSubject("Confirmez votre email");
            helper.setFrom("yassineaounallah22@gmail.com"); // ← mettre votre email
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Échec envoi email");
        }
    }
}
