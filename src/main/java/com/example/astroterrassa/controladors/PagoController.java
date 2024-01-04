package com.example.astroterrassa.controladors;

import com.example.astroterrassa.model.Pago;
import com.example.astroterrassa.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
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
        pagoService.savePago(pago, userId);
        return "redirect:/index";
    }




}