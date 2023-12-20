package com.example.astroterrassa.services;

import com.example.astroterrassa.model.User;
import com.example.astroterrassa.model.UsersRoles;

import java.util.List;

public interface UsuariServiceInterface {
    void guardarUser(User user);
    void guardarRol(User user, UsersRoles rol);
    List<User> llistarUsers(User user);
    User getUserById(Long id);
    User getUserByUsername(String username);

    void registrarPersona(User user, UsersRoles usersRoles);

    void desbloquejarUsuari(Long id,User user);
}