package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.model.User;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepository repo;

    public User save(User user) {
        return userRepository.save(user);
    }


    public void create(User user) {
        // Actualizar el método para guardar todos los campos del formulario
        User newUser = new User();
        newUser.setNombre(user.getNombre());
        newUser.setApellidos(user.getApellidos());
        newUser.setTlf(user.getTlf());
        newUser.setMail(user.getMail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setNotify(user.getNotify());
        repo.save(newUser);
    }
}