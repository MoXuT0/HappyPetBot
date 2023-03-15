package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Report;
import com.team4.happydogbot.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;


@Slf4j
@Service
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Метод сохраняет отчет пользователя
     * @param report
     * @return {@link ReportRepository#save(Object)}
     * @see ReportService
     */
    public Report add(Report report) {
        return this.reportRepository.save(report);
    }

    /**
     * Метод находит и возвращает отчет по id
     * @param id
     * @return {@link ReportRepository#findById(Object)}
     * @throws IllegalArgumentException
     * @see ReportService
     */
    public Report get(long id) {
        return this.reportRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Метод находит и удаляет отчет по id
     * @param id
     * @see ReportService
     */
    public boolean remove(long id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод находит и возвращает все отчеты
     * @return {@link ReportRepository#findAll()}
     * @see ReportService
     */
    public Collection<Report> getAll() {
        return this.reportRepository.findAll();
    }
}
