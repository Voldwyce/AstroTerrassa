package com.example.astroterrassa.DAO;

import com.example.astroterrassa.model.Role;
import com.example.astroterrassa.model.UsersRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRolesRepository extends JpaRepository<UsersRoles, Long>{

    Role findByRolNombre(String rolNombre);

}