package com.example.astroterrassa.DAO;

import com.example.astroterrassa.model.Evento;
import com.example.astroterrassa.model.User;
import com.example.astroterrassa.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

// En UserEventRepository.java
public interface UserEventRepository extends JpaRepository<UserEvent, Long>{
    UserEvent findByUserAndEvento(User user, Evento evento);
}