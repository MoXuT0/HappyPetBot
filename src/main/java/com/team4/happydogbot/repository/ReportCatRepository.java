package com.team4.happydogbot.repository;

import com.team4.happydogbot.entity.ReportCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс, содержащий методы для работы с базой данных отчетов
 * @see ReportCat
 * @see com.team4.happydogbot.service.ReportCatService
 */
@Repository
public interface ReportCatRepository extends JpaRepository<ReportCat, Long> {

}
