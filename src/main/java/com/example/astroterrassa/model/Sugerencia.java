package com.example.astroterrassa.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sugerencias")
@Data
public class Sugerencia {

    @Id
    @Column(name = "id_sugerencia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tipo_sugerencia")
    private String tipoSugerencia;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "sugerencia")
    private String sugerencia_txt;

}
