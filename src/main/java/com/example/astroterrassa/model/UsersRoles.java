package com.example.astroterrassa.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_roles")
public class UsersRoles {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int role_id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "rol_nombre")
    private String rolNombre;

    //Recuperamos de la tabla roles los roles que tiene el usuario
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false)
    private Role role;

    //Recuperamos de la tabla users los usuarios que tienen el rol
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;
}