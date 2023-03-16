package com.team4.happydogbot.repository;

import com.team4.happydogbot.model.Adopter;
import com.team4.happydogbot.entity.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    Adopter findAdopterByChatId(Long id);
}