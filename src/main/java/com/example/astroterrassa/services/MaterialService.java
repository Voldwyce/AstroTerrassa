package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.MaterialRepository;
import com.example.astroterrassa.model.Material;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Material saveMaterial(Material material) {
        return materialRepository.save(material);
    }

    public void updateMaterial(Material material) {
        Material existingMaterial = materialRepository.findById(Long.valueOf(material.getIdMaterial())).orElseThrow(() -> new IllegalArgumentException("Invalid material Id:" + material.getIdMaterial()));
        existingMaterial.setMaterial_nombre(material.getMaterial_nombre());
        existingMaterial.setCantidad(material.getCantidad());
        existingMaterial.setUbicacion(material.getUbicacion());
        materialRepository.save(existingMaterial);
    }

    public byte[] generarCsvMateriales() throws IOException {
        List<Material> materiales = materialRepository.findAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        pw.println("idMaterial,material_nombre,cantidad,ubicacion");

        for (Material material : materiales) {
            pw.printf("%d,%s,%d,%s\n", material.getIdMaterial(), material.getMaterial_nombre(), material.getCantidad(), material.getUbicacion());
        }

        pw.close();
        return baos.toByteArray();
    }

    public byte[] generarPdfMateriales() throws Exception {
        List<Material> materiales = materialRepository.findAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        for (Material material : materiales) {
            document.add(new Paragraph("ID: " + material.getIdMaterial()));
            document.add(new Paragraph("Nombre: " + material.getMaterial_nombre()));
            document.add(new Paragraph("Cantidad: " + material.getCantidad()));
            document.add(new Paragraph("Ubicación: " + material.getUbicacion()));
            document.add(new Paragraph("\n"));
        }

        document.close();
        return baos.toByteArray();
    }
}