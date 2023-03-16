package com.team4.happydogbot.repository;

import com.team4.happydogbot.entity.ReportDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDogRepository extends JpaRepository<ReportDog, Long> {

}
