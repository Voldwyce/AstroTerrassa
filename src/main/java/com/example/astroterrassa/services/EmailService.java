package com.example.astroterrassa.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.model.Pago;
import com.example.astroterrassa.DAO.UserRepository;

import java.io.ByteArrayOutputStream;
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

    public void sendChartEmail(String email, byte[] csvBytes) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Grafico de datos");
            helper.setText("Aqui tienes el csv con los datos del grafico seleccionado!!:");
            helper.addAttachment("chart.csv", new ByteArrayResource(csvBytes));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        emailSender.send(message);
    }


}