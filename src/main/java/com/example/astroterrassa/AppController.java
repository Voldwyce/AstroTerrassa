package com.example.astroterrassa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.astroterrassa.model.*;
import com.example.astroterrassa.DAO.*;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersRolesRepository usersRolesRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @RequestMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/makeRegistration")
    public String makeRegistration(@RequestParam("nombre") String nombre,
                                   @RequestParam("apellidos") String apellidos,
                                   @RequestParam("tlf") String tlf,
                                   @RequestParam("mail") String mail,
                                   @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("notify") int notify) {
        User u = new User();
        u.setNombre(nombre);
        u.setPassword(passwordEncoder.encode(password));
        u.setApellidos(apellidos);
        u.setTlf(tlf);
        u.setMail(mail);
        u.setUsername(username);
        u.setNotify(notify);
        u.setIntents(3);
        userRepository.save(u); // Guarda el usuario primero

        Role r = new Role();
        r.setRolNombre("usuario");
        roleRepository.save(r); // Guarda el rol primero

        UsersRoles ur = new UsersRoles();
        ur.setUser_id(u.getUser_id()); // Ahora el usuario tiene un ID
        ur.setRole_id(r.getRole_id()); // Ahora el rol tiene un ID
        usersRolesRepository.save(ur); // Ahora puedes guardar UsersRoles

        System.out.println("Created user " + u.getUsername() + " with password " + u.getPassword());
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        User user = userRepository.findByUsername(username);
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
}