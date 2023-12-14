package com.example.astroterrassa.controladors;

import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.model.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Slf4j
public class UsuariService implements UserDetailsService {
    @Autowired
    private UserRepository usuariDAO;

    @Override
    @Transactional(readOnly = true) //Consulta només de lectura
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*Implementem el mètode definit en UsuariDao. Hem de pensar que aquest és un mètode predefinit
         *de Spring Security i, per tant, no hem de desnvolupar cap codi, ja ve donat per Spring Security.
         *Aquest mètode ens retornarà l'usuari a partir de nom d'usuari passat per paràmetre.
         */
        com.example.astroterrassa.model.User usuari = usuariDAO.findByUsername(username);

        //Comprovem que existeix l'usuari
        if (usuari == null) { //Si no existeix l'usuari...

            //Llancem una excepció de tipus UsernameNotFoundException
            throw new UsernameNotFoundException(username);

        }


        if(usuari.getIntents()==0){
            log.info("El usuario ha excedido los intentos");
            throw new RuntimeException("Tu usuario se ha quedado sin intentos, contacte con un administrador para poder acceder");
        }

        var rols = new ArrayList<GrantedAuthority>();

        for (Role rol : usuari.getRols()) {
            rols.add(new SimpleGrantedAuthority(rol.getNom()));
        }

        log.info(usuari.getName());
        log.info(usuari.getPassword());
        log.info(rols.get(0).getAuthority());

        return new User(usuari.getName(), usuari.getPassword(), rols);
    }
}