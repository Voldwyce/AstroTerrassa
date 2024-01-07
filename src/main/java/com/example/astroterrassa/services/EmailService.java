package com.example.astroterrassa.services;

import com.example.astroterrassa.model.Sugerencia;
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

import java.util.List;
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

    public void sendSugerencia(Sugerencia sugerencia) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("sarahtree6@gmail.com");
        mail.setSubject("Nueva sugerencia recibida");
        mail.setText("Se ha recibido una nueva sugerencia con el título: " + sugerencia.getTitulo() + "\n" +
                "Tipo de sugerencia: " + sugerencia.getTipoSugerencia() + "\n" +
                "Sugerencia: " + sugerencia.getSugerencia_txt());
        emailSender.send(mail);
    }

    public void sendUserList(List<User> users, String email) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Listado de Usuarios");

        StringBuilder text = new StringBuilder("Listado de Usuarios:\n\n");
        for (User user : users) {
            text.append("Username: ").append(user.getUsername()).append("\n")
                    .append("Nombre: ").append(user.getNombre()).append("\n")
                    .append("Apellidos: ").append(user.getApellidos()).append("\n")
                    .append("Telefono: ").append(user.getTlf()).append("\n")
                    .append("Email: ").append(user.getMail()).append("\n")
                    .append("Notificaciones: ").append(user.getNotify() == 1 ? "Si" : "No").append("\n")
                    .append("Genero: ").append(user.getGenero() == 0 ? "Hombre" : (user.getGenero() == 1 ? "Mujer" : "Otro")).append("\n")
                    .append("Fecha Nacimiento: ").append(user.getFecha_nt()).append("\n\n");
        }

        mail.setText(text.toString());
        emailSender.send(mail);
    }


}