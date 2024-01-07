package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.EventoRepository;
import com.example.astroterrassa.model.Evento;
import com.example.astroterrassa.model.Sugerencia;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.model.Pago;
import com.example.astroterrassa.DAO.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final UserRepository userRepository;
    @Autowired
    private EventoRepository eventoRepository;

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
            MimeMessageHelper mail = new MimeMessageHelper(message, true);

            mail.setTo(email);
            mail.setSubject("Grafico de datos");
            mail.setText("Aqui tienes el csv con los datos del grafico seleccionado!!:");
            mail.addAttachment("chart.csv", new ByteArrayResource(csvBytes));
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


    public byte[] generarCsvEventos() throws IOException {
        List<Evento> eventos = eventoRepository.findAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        pw.println("id,titulo,descripcion,fecha_taller_evento,status");

        for (Evento evento : eventos) {
            pw.printf("%d,%s,%s,%s,%s\n", evento.getId(), evento.getTitulo(), evento.getDescripcion(), evento.getFecha_taller_evento(), evento.getStatus());
        }

        pw.close();
        return baos.toByteArray();
    }

    public void sendEventosList(String email, byte[] csvBytes) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper mail = new MimeMessageHelper(message, true);
            mail.setTo(email);
            mail.setSubject("Lista de Eventos");
            mail.setText("Adjunto encontrarás la lista de eventos en formato CSV.");

            mail.addAttachment("eventos.csv", new ByteArrayResource(csvBytes));
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }

        emailSender.send(message);
    }

    public void sendMaterialesList(String email, byte[] csvBytes) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper mail = new MimeMessageHelper(message, true);
            mail.setTo(email);
            mail.setSubject("Lista de Materiales");
            mail.setText("Adjunto encontrarás la lista de materiales en formato CSV.");

            mail.addAttachment("materiales.csv", new ByteArrayResource(csvBytes));
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }

        emailSender.send(message);
    }

    public void sendMembersiaCaducada(User user) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getMail());
        mail.setSubject("Tu membresia ha caducado");
        mail.setText("Hola " + user.getNombre() + ",\n\nTu membresia ha caducado. Si quieres seguir disfrutando de los beneficios de ser socio, puedes renovarla en la web.\n\nSaludos,\nEl equipo de AstroTerrassa");
        emailSender.send(mail);
    }
}