package com.example.astroterrassa.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.example.astroterrassa.model.User;


@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendWelcomeEmail(User user) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getMail());
        mail.setSubject("Bienvenido a AstroTerrassa");
        mail.setText("Hola " + user.getNombre() + ",\n\nBienvenido a AstroTerrassa. Estamos encantados de tenerte con nosotros.\n\nSaludos,\nEl equipo de AstroTerrassa");
        emailSender.send(mail);
    }
}