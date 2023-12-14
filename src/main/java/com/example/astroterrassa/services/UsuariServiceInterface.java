package com.example.astroterrassa.services;

import com.example.astroterrassa.model.Role;
import com.example.astroterrassa.model.User;
import java.util.List;

public interface UsuariServiceInterface {
    public void guardarUser(User usuari);
    public void guardarRol(User usuari,Role rol);
    public List<User> llistarUsers(User usuari);
    public User getUserById(Long id);
    public User getUserByUsername(String username);

    public void registrarPersona(User user,Role rol,String seleccioRol);

    public void desbloquejarUsuari(Long id,User user);
}