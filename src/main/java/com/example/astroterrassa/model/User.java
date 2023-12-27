package com.example.astroterrassa.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User  {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_user;

    private String nombre;
    private String apellidos;
    private String tlf;
    private String mail;
    private String username;
    private String password;
    private int permisos;
    private int notify;
    private boolean enabled;

    @Transient
    private Role role;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public long getId() {
        return id_user;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}