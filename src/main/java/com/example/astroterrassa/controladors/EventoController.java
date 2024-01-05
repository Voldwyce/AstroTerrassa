package com.example.astroterrassa.controladors;

import com.example.astroterrassa.model.Evento;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.services.EventoService;
import com.example.astroterrassa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String eventos(Model model, @PathVariable int tipo_te) {
        List<Evento> eventos = eventService.getEventosPorTipo(tipo_te);
        User currentUser = userService.getCurrentUser();
        model.addAttribute("titulo", eventos);
        model.addAttribute("tipo_te", tipo_te);
        return "eventos";
    }

}