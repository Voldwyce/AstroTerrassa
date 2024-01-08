package com.example.astroterrassa.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rel_users_te")
@Data
public class EventoPersona {

    @Id
    @Column(name = "id_user_te")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_te")
    private int id_te;

    @Column(name = "id_user")
    private int id_user;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "user_id", insertable = false, updatable = false)  // Referencia a la columna "user_id" en la tabla "users"
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_te", referencedColumnName = "tipo_te", insertable = false, updatable = false)  // Referencia a la columna "tipo_te" en la tabla "taller_evento"
    private Evento evento;

}