package com.example.astroterrassa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;


@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id_user;

    @NotEmpty
    @Column(name="nombre")
    private String name;

    @NotEmpty
    @Column(name="apellidos")
    private String surname;

    @NotEmpty
    @Column(name="tlf")
    private String tlf;

    @NotEmpty
    @Column(name="mail")
    private String mail;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Column(name="permisos")
    private int permisos;

    @NotEmpty
    @Column(name="notify")
    private int notify;

    @NotEmpty
    @Column(name="membresia")
    private Date membresia;

    @OneToMany //Indica al sistema que la relació entre les taules usuari i rol en aquest cas és d'un a molts.
    @JoinColumn(name="user_id") //Columna de la base de dades que farà de clau forana relacionant les dues taules.
    private List<Role> rols;

    @Column(name = "intents")
    private int intents;

    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}