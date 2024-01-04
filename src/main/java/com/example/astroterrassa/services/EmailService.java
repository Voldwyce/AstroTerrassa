package com.example.astroterrassa.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.model.Pago;
import com.example.astroterrassa.DAO.UserRepository;

import java.util.Optional;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final UserRepository userRepository;

    public EmailService(JavaMailSender emailSender , UserRepository userRepository) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;

    }

    public void sendWelcomeEmail(User user) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getMail());
        mail.setSubject("Bienvenido a AstroTerrassa");
        mail.setText("Hola " + user.getNombre() + ",\n\nBienvenido a AstroTerrassa. Estamos encantados de tenerte con nosotros.\n\nSaludos,\nEl equipo de AstroTerrassa");
        emailSender.send(mail);
    }

    public void sendPaymentDetailsEmail(Pago pago, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo("sarahtree6@gmail.com");
            mail.setSubject("Detalles del Pago");
            mail.setText ( "Detalles del Pago:\n" +
                    "Nombre: " + user.getNombre() + "\n" +
                    "Apellidos: " + user.getApellidos() + "\n" +
                    "Email: " + user.getMail() + "\n" +
                    "Cantidad: " + pago.getPrecio() + "\n" +
                    "Número de Cuenta: " + pago.getCuenta() + "\n" +
                    "Fecha: " + pago.getFechaPago() + "\n");

            emailSender.send(mail);
        }
    }

}