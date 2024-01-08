package com.example.astroterrassa.controladors;

import com.example.astroterrassa.DAO.EventoPersonaRepository;
import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.model.Evento;
import com.example.astroterrassa.model.EventoPersona;
import com.example.astroterrassa.model.TipoEvento;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.services.EventoPersonaService;
import com.example.astroterrassa.services.EmailService;
import com.example.astroterrassa.services.EventoService;
import com.example.astroterrassa.services.TipoEventoService;
import com.example.astroterrassa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class EventoController {

    @Autowired
    private EventoService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TipoEventoService tipoEventoService;

    @Autowired
    private EventoPersonaRepository eventoPersonaRepository;

    @Autowired
    private EventoPersonaService eventoPersonaService;

    @GetMapping("/eventos")
    public String showEventos(Model model, Principal principal) {
        User currentUser = userService.getCurrentUser(principal.getName());

        if (currentUser == null) {
            // Handle the case where the User does not exist
            return "redirect:/error";
        }

        List<Evento> eventos = eventService.getAllEventos();
        model.addAttribute("eventos", eventos);

        List<EventoPersona> eventoPersonas = eventoPersonaService.getAllEventoPersonas();
        model.addAttribute("eventos", eventos);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("eventoPersonas", eventoPersonas);
        model.addAttribute("eventoPersona", new EventoPersona()); // Add this line

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        model.addAttribute("currentUser", currentUser);

        return "eventos";
    }

    @RequestMapping("/eventos")
    public String eventos(Model model, @PathVariable int tipoEvento) {
        Evento eventos = eventService.getEventosPorTipo(tipoEvento);
        model.addAttribute("eventos", eventos);
        model.addAttribute("tipoEvento", tipoEvento);


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User currentUser = userService.getCurrentUser(username);

        model.addAttribute("currentUser", currentUser);

        return "eventos";
    }

    @GetMapping("/nuevoEvento")
    public String nuevoEvento(Model model, Principal principal) {

        List<TipoEvento> tiposEvento = tipoEventoService.getAllTiposEvento();

        String username = principal.getName();

        User currentUser = userRepository.findByUsername(username);

        model.addAttribute("tiposEvento", tiposEvento);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("evento", new Evento());

        return "nuevoEvento";
    }

    @PostMapping("/nuevoEvento")
    public String saveEvento(@ModelAttribute Evento evento, @RequestParam String fecha_taller_evento, @RequestParam(value = "statusInt", required = false) Boolean statusInt) throws Exception {
        // Convert the fecha_taller_evento string to a Date object
        Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_taller_evento);
        evento.setFecha_taller_evento(fecha);

        // Convert the statusInt Boolean to an int
        int status = (statusInt != null && statusInt) ? 1 : 0;
        evento.setStatus(status);

        // Save the Evento object
        eventService.saveEvento(evento);

        return "redirect:/eventos";
    }

    @GetMapping("/listadoTipoEvento")
    public String showListado(Model model, Principal principal) {
        User currentUser = userService.getCurrentUser(principal.getName());
        List<TipoEvento> tipoEvento = tipoEventoService.getAllTiposEvento();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("tiposEvento", tipoEvento);
        return "listadoTipoEvento";
    }

    @GetMapping("/crearTipoEvento")
    public String showForm(Model model) {
        model.addAttribute("tipoEvento", new TipoEvento());
        return "crearTipoEvento";
    }

    @PostMapping("/crearTipoEvento")
    public String crearTipoEvento(@ModelAttribute TipoEvento tipoEvento) {
        tipoEventoService.saveTipoEvento(tipoEvento);
        return "redirect:/listadoTipoEvento";
    }

    @PostMapping("/eliminarTipoEvento/{id}")
    public String deleteTipoEvento(@PathVariable("id") int id) {
        tipoEventoService.deleteTipoEvento(id);
        return "redirect:/eventos";
    }

    @PostMapping("/inscribirse")
    public String inscribirse(@ModelAttribute("eventoPersona") EventoPersona eventoPersona, Principal principal) {
        User currentUser = userService.getCurrentUser(principal.getName());

        Evento evento = eventService.getEventosPorTipo(eventoPersona.getId_te());
        eventoPersona.setId_te(evento.getTipo()); // Set id_te to the tipo of the Evento
        eventoPersona.setId_user((int) currentUser.getId());

        eventoPersonaService.saveEventoPersona(eventoPersona);
        eventoPersonaRepository.save(eventoPersona);

        return "redirect:/eventos";
    }

    @GetMapping("/sendEventosList")
    public ResponseEntity<String> sendEventosList(@RequestParam String email) throws IOException {
        byte[] csvBytes = emailService.generarCsvEventos();
        emailService.sendEventosList(email, csvBytes);

        return new ResponseEntity<>("Lista de eventos enviada", HttpStatus.OK);
    }
}