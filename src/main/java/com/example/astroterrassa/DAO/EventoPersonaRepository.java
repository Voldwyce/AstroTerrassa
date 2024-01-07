package com.example.astroterrassa.DAO;

import com.example.astroterrassa.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoPersonaRepository extends JpaRepository<Evento, Long> {


}