package com.example.astroterrassa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_user;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany //Indica al sistema que la relació entre les taules usuari i rol en aquest cas és d'un a molts.
    @JoinColumn(name="user_id") //Columna de la base de dades que farà de clau forana relacionant les dues taules.
    private List<Role> rols;

    @Column(name = "intentos")
    private int intents;
    private int notify;
    private boolean enabled;
    private int genero;

    @Column(name = "register_dt")
    private LocalDate register_dt;

    private String last_dt;

    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

}