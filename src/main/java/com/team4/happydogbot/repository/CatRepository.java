package com.team4.happydogbot.repository;

import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс, содержащий методы для работы с базой данных животных
 * @see Cat
 * @see com.team4.happydogbot.service.CatService
 */
public interface CatRepository extends JpaRepository<Cat, Long> {

}
