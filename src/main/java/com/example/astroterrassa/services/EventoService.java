package com.example.astroterrassa.services;

import com.example.astroterrassa.DAO.EventoRepository;
import com.example.astroterrassa.model.Evento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> getEventosPorTipo(int tipo) {
        return eventoRepository.findByTipo(tipo);
    }
}