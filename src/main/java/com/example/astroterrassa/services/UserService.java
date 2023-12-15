package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.RolDao;
import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.model.Role;
import com.example.astroterrassa.model.User;

import java.io.ByteArrayOutputStream;
import java.util.List;
import jakarta.transaction.Transactional;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class UserService implements UsuariServiceInterface {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private UserRepository repo;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Autowired
    private RolDao rolDao;

    @Override
    @Transactional
    public void guardarUser(User usuari) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuari.setPassword(passwordEncoder.encode(usuari.getPassword()));
        usuari.setIntents(3);
        this.userRepository.save(usuari);

    }

    @Override
    public void guardarRol(User usuari, Role rol) {
        rol.setIdUsuari(usuari);
        rolDao.save(rol);
    }

    @Override
    public List<User> llistarUsers(User usuari) {
        return userRepository.findAll();

    }

    @Transactional(readOnly = true)
    public List<User> getBlockedUsers() {
        return userRepository.blockedUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);

    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void registrarPersona(User user, Role rol, String seleccioRol) {
        guardarUser(user);
        rol.setNom(seleccioRol);
        guardarRol(user, rol);
        System.out.println("User guardado");
    }

    @Override
    public void desbloquejarUsuari(Long id, User user) {
        user = getUserById(id);
        user.setIntents(3);
        userRepository.save(user);
        System.out.println("S'ha desbloquejat l'usuari");
    }

    public User save(User user) {
        return userRepository.save(user);
    }


        public String generateHtmlTable() {
            List<User> users = getAllUsers();
            StringBuilder htmlTable = new StringBuilder("<table><tr><th>Nombre</th><th>Apellidos</th><th>Telefono</th><th>Email</th><th>Notificaciones</th><th>Username</th></tr>");
            for (User user : users) {
                htmlTable.append("<tr><td>")
                        .append(user.getNombre())
                        .append("</td><td>")
                        .append(user.getApellidos())
                        .append("</td><td>")
                        .append(user.getTlf())
                        .append("</td><td>")
                        .append(user.getMail())
                        .append("</td><td>")
                        .append(user.getNotify())
                        .append("</td><td>")
                        .append(user.getUsername())
                        .append("</td></tr>");
            }
            htmlTable.append("</table>");
            return htmlTable.toString();
        }

        public byte[] createPdfFromHtmlTable(String htmlTable) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(htmlTable, outputStream);
            return outputStream.toByteArray();
        }

    }



