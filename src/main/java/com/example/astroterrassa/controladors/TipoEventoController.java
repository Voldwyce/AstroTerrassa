package com.example.astroterrassa.controladors;

import com.example.astroterrassa.model.TipoEvento;
import com.example.astroterrassa.services.TipoEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TipoEventoController {

    @Autowired
    private TipoEventoService tipoEventoService;

    @GetMapping("/listadoTiposEvento")
    public String showListado(Model model) {
        List<TipoEvento> tiposEvento = tipoEventoService.getAllTiposEvento();
        model.addAttribute("tiposEvento", tiposEvento);
        return "listadoTiposEvento";
    }

    @PostMapping("/eliminarTipoEvento/{id}")
    public String deleteTipoEvento(@PathVariable("id") int id) {
        tipoEventoService.deleteTipoEvento(id);
        return "redirect:/listadoTiposEvento";
    }
}