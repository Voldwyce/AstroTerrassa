package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.EventoRepository;
import com.example.astroterrassa.DAO.UserRepository;
import com.example.astroterrassa.model.Evento;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.model.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.astroterrassa.DAO.UserEventRepository;


@Service
public class UserEventService {

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventoRepository eventoRepository;

    public void deleteUserEvent(String username, int id_te) {
        User user = userRepository.findByUsername(username);
        Evento evento = eventoRepository.findById((long) id_te).orElse(null);
        if (evento != null) {
            UserEvent userEvent = userEventRepository.findByUserAndEvento(user, evento);
            userEventRepository.delete(userEvent);
        }
    }
}