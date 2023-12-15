package com.example.astroterrassa.Conroller;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class editPerfilController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/editPerfil")
    public String showEditForm(@RequestParam(value = "username", required = false) String username, Model model) {
        if (username != null) {
            User user = userRepository.findByUsername(username);
            model.addAttribute("user", user);
        }
        return "editPerfil";
    }

    @PostMapping("/editPerfil")
    public String submitEditForm(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/index";
    }
}
