package com.example.astroterrassa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.astroterrassa.model.*;
import com.example.astroterrassa.DAO.*;
import com.example.astroterrassa.services.EmailService;

import java.time.ZonedDateTime;
import java.util.Date;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersRolesRepository usersRolesRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EmailService emailService;
    @RequestMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/makeRegistration")
    public String makeRegistration(@RequestParam("nombre") String nombre,
                                   @RequestParam("apellidos") String apellidos,
                                   @RequestParam("tlf") String tlf,
                                   @RequestParam("mail") String mail,
                                   @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("fecha_nt") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha_nt,
                                   @RequestParam("notify") int notify,
                                   @RequestParam("genero") int genero) {
        User u = new User();
        u.setNombre(nombre);
        u.setPassword(passwordEncoder.encode(password));
        u.setApellidos(apellidos);
        u.setTlf(tlf);
        u.setMail(mail);
        u.setUsername(username);
        u.setFecha_nt(fecha_nt);
        u.setGenero(genero);
        u.setPermisos(1);
        u.setNotify(notify);
        u.setIntents(3);
        u.setLastDt(Date.from(ZonedDateTime.now().toInstant()));
        userRepository.save(u); // Guarda el usuario primero

        if (notify == 1) {
        emailService.sendWelcomeEmail(u);
        }


        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setUserId(u.getUser_id());
        usersRoles.setRoleId(0);
        usersRoles.setRolNombre("usuario");
        usersRolesRepository.save(usersRoles); // Guarda el rol del usuario

        System.out.println("Created user " + u.getUsername() + " with password " + u.getPassword());
        return "redirect:/index";
    }

    @RequestMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {

        User user = userRepository.findByUsername(username);
        user.setLastDt(Date.from(ZonedDateTime.now().toInstant()));
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return "index";
        } else {
            return "redirect:/login";
        }
    }

    // Método para asignar roles solo uno a cada usuario, accesible solo para administradores
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/asignarRol")
    public String asignarRol(@RequestParam("username") String username,
                             @RequestParam("rol") String rol) {
        User user = userRepository.findByUsername(username);
        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setRolNombre(rol);
        usersRolesRepository.save(usersRoles);
        userRepository.save(user);
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/stats")
    public String showStats() {
        return "stats";
    }
}