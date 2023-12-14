package com.example.astroterrassa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long idRol;

    @NotEmpty
    private String nom;

    @ManyToOne
    @JoinColumn(name="id_usuari",nullable=false)
    private User idUsuari;
}
