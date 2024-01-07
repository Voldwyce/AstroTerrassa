package com.example.astroterrassa.controladors;

import com.example.astroterrassa.model.Material;
import com.example.astroterrassa.services.EmailService;
import com.example.astroterrassa.services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.astroterrassa.DAO.MaterialRepository;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/material")
    public String showMaterialForm(@RequestParam(required = false) Long id, Model model) {
        List<Material> materiales = materialRepository.findAll();
        model.addAttribute("materiales", materiales);

        Material material;
        if (id != null) {
            material = materialRepository.findById(id).orElse(new Material());
        } else {
            material = new Material();
        }
        model.addAttribute("material", material);

        return "material";
    }

    @PostMapping("/material")
    public String saveMaterial(@ModelAttribute Material material, Model model) {
        if (material.getIdMaterial() == 0) {
            materialService.saveMaterial(material);
        } else {
            materialService.updateMaterial(material);
        }
        return showMaterialForm(null, model);
    }

    @GetMapping("/materialDetails")
    @ResponseBody
    public Material getMaterialDetails(@RequestParam Long id) {
        return materialRepository.findById(id).orElse(new Material());
    }

    @GetMapping("/sendMaterialesList")
    public ResponseEntity<String> sendMaterialesList(@RequestParam String email) throws IOException {
        byte[] csvBytes = new byte[0];
        try {
            csvBytes = materialService.generarCsvMateriales();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emailService.sendMaterialesList(email, csvBytes);

        return new ResponseEntity<>("Lista de materiales enviada", HttpStatus.OK);
    }

    @GetMapping("/downloadMaterialesList")
    public ResponseEntity<byte[]> downloadMaterialesList() throws Exception {
        byte[] pdfBytes = materialService.generarPdfMateriales();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "materiales.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}