package com.example.astroterrassa.controladors;

import com.example.astroterrassa.model.Evento;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.services.EventoService;
import com.example.astroterrassa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EventoController {

    @Autowired
    private EventoService eventService;

    @Autowired
    private UserService userService;

    @GetMapping("/eventos")
    public String showEventos() {
        return "eventos";
    }

    @RequestMapping("/eventos")
    public String eventos(Model model, @PathVariable int tipoEvento) {
        List<Evento> eventos = eventService.getEventosPorTipo(tipoEvento);
        model.addAttribute("eventos", eventos);
        model.addAttribute("tipoEvento", tipoEvento);

        // Get the current user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User currentUser = userService.getCurrentUser(username);

        // Add the current user to the model
        model.addAttribute("currentUser", currentUser);

        return "eventos";
    }

    @GetMapping("/nuevoEvento")
    public String showNewEventoForm(Model model) {
        Evento evento = new Evento();
        model.addAttribute("evento", evento);
        return "nuevoEvento";
    }

    @PostMapping("/nuevoEvento")
    public String saveEvento(@ModelAttribute("evento") Evento evento) {
        eventService.saveEvento(evento);
        return "redirect:/eventos";
    }
}