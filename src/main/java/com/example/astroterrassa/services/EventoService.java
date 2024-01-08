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

    public Evento getEventosPorTipo(int tipo) {
        return eventoRepository.findByTipo(tipo);
    }

    public void saveEvento(Evento evento) {
        eventoRepository.save(evento);
    }

    public List<Evento> getAllEventos() {
        return eventoRepository.findAll();
    }

    public Evento getEventoById(int id) {
        return eventoRepository.findByTipo(id);
    }
}