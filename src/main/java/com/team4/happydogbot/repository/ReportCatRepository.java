package com.team4.happydogbot.repository;

import com.team4.happydogbot.entity.ReportCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReportCatRepository extends JpaRepository<ReportCat, Long> {

}
