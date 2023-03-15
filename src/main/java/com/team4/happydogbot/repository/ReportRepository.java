package com.team4.happydogbot.repository;

import com.team4.happydogbot.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedList;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
