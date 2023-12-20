package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.DAO.UsersRolesRepository;
import com.example.astroterrassa.model.User;
import java.util.List;

import com.example.astroterrassa.model.UsersRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UsuariServiceInterface {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepository repo;

    @Autowired
    private UsersRolesRepository usersRolesRepository;

    @Override
    @Transactional
    public void guardarUser(User usuari) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuari.setPassword(passwordEncoder.encode(usuari.getPassword()));
        usuari.setIntents(3);
        this.userRepository.save(usuari);

    }

    // Método para guardar el rol del usuario
    @Override
    public void guardarRol(User usuari, UsersRoles rol) {
        rol.setUserId(usuari.getUser_id());
        rol.setRoleId(rol.getRoleId());
        rol.setRolNombre(rol.getRolNombre());
        usersRolesRepository.save(rol);
        System.out.println("Rol guardado");
    }

    @Override
    public List<User> llistarUsers(User usuari) {
        return userRepository.findAll();

    }

    @Transactional(readOnly = true)
    public List<User> getBlockedUsers() {
        return userRepository.blockedUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);

    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void registrarPersona(User user, UsersRoles usersRoles) {
        // Crea y guarda el usuario
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIntents(3);
        User savedUser = userRepository.save(user); // Guarda el usuario en la base de datos

        // Asigna el User_id y el Role_id a UsersRoles y lo guarda
        usersRoles.setUserId(savedUser.getUser_id());
        usersRoles.setRoleId(0);
        usersRoles.setRolNombre("usuario");
        usersRolesRepository.save(usersRoles); // Guarda el rol del usuario

        System.out.println("User guardado");
    }

    @Override
    public void desbloquejarUsuari(Long id, User user) {
        user = getUserById(id);
        user.setIntents(3);
        userRepository.save(user);
        System.out.println("S'ha desbloquejat l'usuari");
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public UsersRoles save(UsersRoles usersRoles) {
        return usersRolesRepository.save(usersRoles);
    }

}