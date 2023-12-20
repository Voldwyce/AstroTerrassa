package com.example.astroterrassa.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;


@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int user_id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "mail")
    private String mail;

    @Column(name = "tlf")
    private String tlf;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "notify")
    private int notify;

    @Column(name = "intentos")
    private int intents;

    //Recuperamos de la tabla users_roles el rol que tiene el usuario
    @OneToOne(mappedBy = "user")
    private UsersRoles usersRoles;

}