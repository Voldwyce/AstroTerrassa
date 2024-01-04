package com.example.astroterrassa.controladors;

import com.example.astroterrassa.model.Pago;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.services.EmailService;
import com.example.astroterrassa.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PagoController {

    private final PagoService pagoService;

    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @Autowired
    private EmailService emailService;

    @GetMapping("/pago")
    public String getPagoForm(Model model) {
        model.addAttribute("pago", new Pago());
        return "pago";
    }

    @PostMapping("/pago")
    public String submitPagoForm(@ModelAttribute Pago pago, @RequestParam Long userId) {
        if (pago.getProducto().equals("Joven")) {
            pago.setPrecio(20);
        } else if (pago.getProducto().equals("Normal")) {
            pago.setPrecio(40);
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User currentUser = UsuariServices.getUserByUsername(username);

        pagoService.savePago(pago, userId);
        Pago savedPago = pagoService.savePago(pago, userId);
        emailService.sendPaymentDetailsEmail(savedPago, userId);

        return "redirect:/index";
    }




}