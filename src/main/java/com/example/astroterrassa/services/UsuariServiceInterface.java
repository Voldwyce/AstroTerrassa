package com.example.astroterrassa.services;

import com.example.astroterrassa.model.Role;
import com.example.astroterrassa.model.User;
import java.util.List;

public interface UsuariServiceInterface {
    void guardarUser(User usuari);
    void guardarRol(User usuari, Role rol);
    List<User> llistarUsers(User usuari);
    User getUserById(Long id);
    User getUserByUsername(String username);

    void registrarPersona(User user,Role rol,String seleccioRol);

    void desbloquejarUsuari(Long id,User user);
}