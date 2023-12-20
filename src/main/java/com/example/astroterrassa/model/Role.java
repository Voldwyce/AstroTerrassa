package com.example.astroterrassa.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int role_id;

    @Column(name = "user_id")
    private String user_id;

    @Column(name = "rol_nombre")
    private String rolNombre;

}