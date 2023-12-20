package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.RoleRepository;
import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.DAO.UsersRolesRepository;
import com.example.astroterrassa.model.Role;
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

    @Autowired
    private RoleRepository roleRepository;

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
        rol.setUser_id(usuari.getUser_id());
        rol.setRole_id(rol.getRole_id());
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
    public void registrarPersona(User user, UsersRoles rol, String seleccioRol) {
        // Guarda el usuario
        guardarUser(user);

        // Crea y guarda el rol
        Role role = new Role();
        role.setRolNombre(seleccioRol);
        Role savedRole = save(role);

        // Asigna el usuario y el rol a UsersRoles y lo guarda
        rol.setUser_id(user.getUser_id());
        rol.setRole_id(savedRole.getRole_id());
        guardarRol(user, rol);

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

    public Role save(Role role) {
        return roleRepository.save(role);
    }

}