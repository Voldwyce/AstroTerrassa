package com.example.astroterrassa.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

import lombok.Data;


@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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

    @Column(name = "permisos")
    private Integer permisos;

    //Recuperamos de la tabla users_roles el rol que tiene el usuario
    @OneToOne(mappedBy = "user")
    private UsersRoles usersRoles;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthenticationType authType;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "register_dt")
    private Date registerDt;

    @Column(name = "genero")
    private int genero;

    @Column(name = "last_dt")
    private Date lastDt;

    @Column(name = "fecha_nt")
    private Date fecha_nt;

    public long getId() {
        return user_id;
    }


}