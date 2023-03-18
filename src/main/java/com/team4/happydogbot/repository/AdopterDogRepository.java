package com.team4.happydogbot.repository;

import com.team4.happydogbot.entity.AdopterDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс, содержащий методы для работы с базой данных усыновителей
 * @see AdopterDog
 * @see com.team4.happydogbot.service.AdopterDogService
 */
@Repository
public interface AdopterDogRepository extends JpaRepository<AdopterDog, Long> {

}