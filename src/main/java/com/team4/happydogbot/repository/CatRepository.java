package com.team4.happydogbot.repository;

import com.team4.happydogbot.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat, Long> {

}
