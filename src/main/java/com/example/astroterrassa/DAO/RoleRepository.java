package com.example.astroterrassa.DAO;

import com.example.astroterrassa.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}