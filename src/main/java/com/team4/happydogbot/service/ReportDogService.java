package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.ReportDog;
import com.team4.happydogbot.repository.ReportDogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;


@Slf4j
@Service
@Transactional
public class ReportDogService {
    private final ReportDogRepository reportRepository;

    public ReportDogService(ReportDogRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Метод сохраняет отчет пользователя
     * @param reportDog
     * @return {@link ReportDogRepository#save(Object)}
     * @see ReportDogService
     */
    public ReportDog add(ReportDog reportDog) {
        return this.reportRepository.save(reportDog);
    }

    /**
     * Метод находит и возвращает отчет по id
     * @param id
     * @return {@link ReportDogRepository#findById(Object)}
     * @throws IllegalArgumentException
     * @see ReportDogService
     */
    public ReportDog get(Long id) {
        return this.reportRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Метод находит и удаляет отчет по id
     * @param id
     * @see ReportDogService
     */
    public boolean remove(Long id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод находит и возвращает все отчеты
     * @return {@link ReportDogRepository#findAll()}
     * @see ReportDogService
     */
    public Collection<ReportDog> getAll() {
        return this.reportRepository.findAll();
    }
}
