package com.team4.happydogbot.repository;

import com.team4.happydogbot.entity.AdopterCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdopterCatRepository extends JpaRepository<AdopterCat, Long> {
    AdopterCat findAdopterByChatId(Long id);
}
