package com.team4.happydogbot.repository;

import com.team4.happydogbot.model.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    Adopter findAdopterByChatId(Long id);
}