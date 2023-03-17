package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.ReportCat;
import com.team4.happydogbot.exceptions.ReportCatNotFoundException;
import com.team4.happydogbot.repository.ReportCatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Collection;


@Slf4j
@Service
@Transactional
public class ReportCatService {
    private final ReportCatRepository reportCatRepository;

    public ReportCatService(ReportCatRepository reportRepository) {
        this.reportCatRepository = reportRepository;
    }

    /**
     * Метод сохраняет отчет пользователя
     * @param reportCat
     * @return {@link ReportCatRepository#save(Object)}
     * @see ReportCatService
     */
    public ReportCat add(ReportCat reportCat) {
        return this.reportCatRepository.save(reportCat);
    }

    /**
     * Метод находит и возвращает отчет по id
     * @param id
     * @return {@link ReportCatRepository#findById(Object)}
     * @throws ReportCatNotFoundException
     * @see ReportCatService
     */
    public ReportCat get(Long id) {
        return this.reportCatRepository.findById(id)
                .orElseThrow(ReportCatNotFoundException::new);
    }

    /**
     * Метод находит и удаляет отчет по id
     * @param id
     * @see ReportCatService
     */
    public boolean remove(Long id) {
        if (reportCatRepository.existsById(id)) {
            reportCatRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод находит и возвращает все отчеты
     * @return {@link ReportCatRepository#findAll()}
     * @see ReportCatService
     */
    public Collection<ReportCat> getAll() {
        return this.reportCatRepository.findAll();
    }
}
