package com.example.astroterrassa.controladors;

import com.example.astroterrassa.DAO.EventoPersonaRepository;
import com.example.astroterrassa.DAO.UserEventRepository;
import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.model.*;
import com.example.astroterrassa.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

    @Autowired
    private UserEventService userEventService;

    @Autowired
    private UserEventRepository userEventRepository;

    @GetMapping("/eventos")
    public String showEventos(Model model, Principal principal) {
        User currentUser = userService.getCurrentUser(principal.getName());

        if (currentUser == null) {
            // Handle the case where the User does not exist
            return "redirect:/error";
        }

        List<Evento> eventos = eventService.getAllEventos();
        for (Evento evento : eventos) {
            UserEvent userEvent = userEventRepository.findByUserAndEvento(currentUser, evento);
            evento.setUserInscribed(userEvent != null);
        }
        model.addAttribute("eventos", eventos);

        List<EventoPersona> eventoPersonas = eventoPersonaService.getAllEventoPersonas();
        model.addAttribute("eventos", eventos);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("eventoPersonas", eventoPersonas);
        model.addAttribute("eventoPersona", new EventoPersona());

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
    public String saveEvento(@ModelAttribute Evento evento, @RequestParam String fecha_taller_evento, @RequestParam(value = "statusInt", required = false) Boolean statusInt, @RequestParam("id") int tipoId) throws Exception {
        // Convert the fecha_taller_evento string to a Date object
        Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_taller_evento);
        evento.setFecha_taller_evento(fecha);

        // Convert the statusInt Boolean to an int
        int status = (statusInt != null && statusInt) ? 1 : 0;
        evento.setStatus(status);

        // Set the tipo_id value
        evento.setTipo(tipoId);

        // Find title with id
        TipoEvento tipoEvento = tipoEventoService.getTipoEventoById(tipoId);
        String titulo = tipoEvento.getTitulo();
        evento.setTitulo(titulo);

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
    public String inscribirse(@RequestParam("idEvento") int idEvento, Principal principal, Model model, RedirectAttributes redirectAttributes) {
        // Get the Evento
        Evento evento = eventService.getEventoById(idEvento);

        // Check the status of the Evento
        if (evento.getStatus() == 0) {
            // If the status is 0, redirect to an error page and show an error message
            redirectAttributes.addFlashAttribute("error", "No se puede inscribir en este evento porque ya ha pasado.");
            return "redirect:/error403";
        }

        // Get the current User
        User currentUser = userService.getCurrentUser(principal.getName());

        // Create a new EventoPersona object
        EventoPersona eventoPersona = new EventoPersona();
        eventoPersona.setId_te(idEvento);
        eventoPersona.setId_user((int) currentUser.getId());

        // Save the EventoPersona object
        eventoPersonaService.saveEventoPersona(eventoPersona);

        if (currentUser.getNotify() == 1) {
            emailService.sendInscripcionEvento(currentUser.getMail(), evento.getTitulo());
        }

        return "redirect:/eventos";
    }

    @PostMapping("/desinscribirse")
    public String desinscribirse(@RequestParam("idEvento") int idEvento, Principal principal) {
        User currentUser = userService.getCurrentUser(principal.getName());
        userEventService.deleteUserEvent(currentUser.getUsername(), idEvento);
        return "redirect:/eventos";
    }

    @GetMapping("/eventos/pdf")
    public ResponseEntity<byte[]> getEventosPdf() {
        // Generate the PDF bytes (this method should be implemented in your service)
        byte[] pdfBytes = eventService.generatePdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "eventos.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/sendEventosList")
    public ResponseEntity<String> sendEventosList(@RequestParam String email) throws IOException {
        byte[] csvBytes = emailService.generarCsvEventos();
        emailService.sendEventosList(email, csvBytes);

        return new ResponseEntity<>("Lista de eventos enviada", HttpStatus.OK);
    }
}