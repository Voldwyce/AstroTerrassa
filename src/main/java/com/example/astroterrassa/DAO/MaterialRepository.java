package com.example.astroterrassa.DAO;

import com.example.astroterrassa.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}
