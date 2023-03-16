package com.team4.happydogbot.repository;

import com.team4.happydogbot.entity.AdopterDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdopterDogRepository extends JpaRepository<AdopterDog, Long> {
    AdopterDog findAdopterByChatId(Long id);
}