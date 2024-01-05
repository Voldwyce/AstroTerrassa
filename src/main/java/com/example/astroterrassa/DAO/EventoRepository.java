package com.example.astroterrassa.DAO;

import com.example.astroterrassa.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByTipo(int tipo);
}