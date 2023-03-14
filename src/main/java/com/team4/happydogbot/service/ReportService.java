package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Report;
import com.team4.happydogbot.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportService {
    private final ReportRepository reportRepository;


    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report add(Report report) {
        return this.reportRepository.save(report);
    }

    public Report get(long id) {
        return this.reportRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
