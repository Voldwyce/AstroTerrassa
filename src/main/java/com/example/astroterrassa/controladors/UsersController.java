package com.example.astroterrassa.controladors;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.model.Role;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.services.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class UsersController {
    @Autowired
    private UserService UsuariServices;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // your code here
        return "register";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @PostMapping("/register")
    public String registre2(User user,Role rol,@RequestParam(name = "seleccioRol")String rolSeleccio){
        UsuariServices.registrarPersona(user, rol, rolSeleccio);
        return "redirect:/login";
    }

    @GetMapping("/llistaUsuaris")
    public String llistaUsuaris(User user,Model model){
        List<User> listaUsers = UsuariServices.llistarUsers(user);
        model.addAttribute("usuaris",listaUsers);
        return "llistaUsuaris";
    }
    @GetMapping("/bloquejats")
    public String llistaUsuarisBloquejats(User user,Model model){
        List<User> listaUsers = UsuariServices.getBlockedUsers();
        model.addAttribute("usuaris",listaUsers);
        return "llistaUsuaris";
    }

    @GetMapping("/desbloqueja/{id}")
    public String desbloquejarUsuari(@PathVariable Long id,User user){
        UsuariServices.desbloquejarUsuari(id, user);
        return "redirect:/llistaUsuaris";

    }
}