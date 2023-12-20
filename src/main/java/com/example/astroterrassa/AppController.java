package com.example.astroterrassa;

import org.springframework.beans.factory.annotation.Autowired;
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

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
                                   @RequestParam("notify") int notify) {
        User u = new User();
        u.setUsername(nombre);
        u.setPassword(passwordEncoder.encode(password));
        userRepository.save(u);
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

    @RequestMapping("/index")
    public String showIndex() {
        return "index";
    }
}