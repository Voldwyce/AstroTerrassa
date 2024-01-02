package com.example.astroterrassa.controladors;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.DAO.UsersRolesRepository;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.model.UsersRoles;
import com.example.astroterrassa.services.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class UsersController {
    @Autowired
    private UserService UsuariServices;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersRolesRepository UsersRolesRepository;

    @RequestMapping("/listado")
    @GetMapping
    public ModelAndView getAllUsers() {
        ModelAndView mav = new ModelAndView("listado");
        mav.addObject("users", UsuariServices.getAllUsers());
        return mav;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registre(User user, UsersRoles usersRoles){
        UsuariServices.registrarPersona(user, usersRoles);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
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