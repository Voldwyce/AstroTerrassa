

package com.example.astroterrassa.Conroller;

import com.example.astroterrassa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping("/listado")
    @GetMapping
    public ModelAndView getAllUsers() {
        ModelAndView mav = new ModelAndView("listado");
        mav.addObject("users", userService.getAllUsers());
        return mav;
    }

    @GetMapping("/persons/pdf")
    public ResponseEntity<byte[]> getPersonsPdf() {
        // Aquí debes generar tu tabla HTML como una cadena
        String htmlTable = userService.generateHtmlTable();

        // Luego, llama a la función createPdfFromHtmlTable para convertir la tabla HTML a PDF
        byte[] pdfBytes = userService.createPdfFromHtmlTable(htmlTable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("person-list.pdf", "person-list.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


}