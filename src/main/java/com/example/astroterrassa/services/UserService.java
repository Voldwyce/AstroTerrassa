package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.DAO.RoleRepository;

import com.example.astroterrassa.model.Role;
import com.example.astroterrassa.model.User;
import jakarta.transaction.Transactional;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepository;


    public List<User> getAllUsers()  {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Optional<Role> role = roleRepository.findById(user.getPermisos());
            role.ifPresent(user::setRole);
        }
        return users;
    }


    public User save(User user) {
        return userRepository.save(user);
    }

    public void create(User user) {
        // Actualizar el método para guardar todos los campos del formulario
        User newUser = new User();
        newUser.setNombre(user.getNombre());
        newUser.setApellidos(user.getApellidos());
        newUser.setTlf(user.getTlf());
        newUser.setMail(user.getMail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setNotify(user.getNotify());
        repo.save(newUser);
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



