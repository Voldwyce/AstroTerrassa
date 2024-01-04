package com.example.astroterrassa.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "taller_evento")
@Data
public class Evento {

    @Id
    @Column(name = "id_te")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_te;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_taller_evento")
    private Date fecha_taller_evento;

    @Column(name = "status")
    private int status;

    @Column(name = "tipo_te")
    private int tipo;

}
