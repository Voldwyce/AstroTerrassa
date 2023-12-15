package com.example.astroterrassa.controladors;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.model.Role;
import com.example.astroterrassa.model.User;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Slf4j
public class UsuariService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UsuariService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true) //Consulta només de lectura
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usuari = userRepository.findByUsername(username);

        //Comprovem que existeix l'usuari
        if (usuari == null) {
            throw new UsernameNotFoundException("User not found");
        }


        if(usuari.getIntents()==0){
            log.info("El usuario ha excedido los intentos");
            throw new RuntimeException("Tu usuario se ha quedado sin intentos, contacte con un administrador para poder acceder");
        }

        var rols = new ArrayList<GrantedAuthority>();

        for (Role rol : usuari.getRols()) {
            rols.add(new SimpleGrantedAuthority(rol.getNom()));
        }

        log.info(usuari.getUsername());
        log.info(usuari.getPassword());

        return new org.springframework.security.core.userdetails.User(usuari.getUsername(), usuari.getPassword(), new ArrayList<>());
    }
}