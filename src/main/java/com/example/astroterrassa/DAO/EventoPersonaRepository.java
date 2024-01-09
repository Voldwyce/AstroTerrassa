package com.example.astroterrassa.DAO;

import com.example.astroterrassa.model.Evento;
import com.example.astroterrassa.model.EventoPersona;
import com.example.astroterrassa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoPersonaRepository extends JpaRepository<EventoPersona, Long> {
    List<EventoPersona> findAllByEvento(Evento evento);

    EventoPersona findByUserAndEvento(User user, Evento evento);

}