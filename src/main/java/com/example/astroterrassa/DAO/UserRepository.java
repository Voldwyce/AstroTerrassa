package com.example.astroterrassa.DAO;

import com.example.astroterrassa.model.AuthenticationType;
import com.example.astroterrassa.model.User;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;



import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.intents = 0")
    List<User> blockedUsers();

    @Modifying
    @Query("UPDATE User u SET u.authType = ?2 WHERE u.username = ?1")
    void updateAuthenticationType(String username, AuthenticationType authType);

    @Modifying
    @Query("INSERT INTO User (username, password, authType) VALUES (?1, ?2, ?3)")
    void createUser(String username, String password, AuthenticationType authType);

    @Query("SELECT u FROM User u WHERE u.mail = ?1")
    User findByMail(String email);

    @Modifying
    @Query("UPDATE User u SET u.nombre = ?1, u.apellidos = ?2, u.mail = ?3, u.tlf = ?4, u.notify = ?5, u.genero = ?6, u.fecha_nt = ?7 WHERE u.username = ?8")
    void updateUserDetails(String nombre, String apellidos, String mail, String tlf, int notify, int genero, Date fecha_nt, String username);
}