package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.ReportCat;
import com.team4.happydogbot.exception.ReportCatNotFoundException;
import com.team4.happydogbot.repository.ReportCatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Collection;

/**
 *Класс - сервис, содержащий набор CRUD операций над объектом ReportCat
 * @see ReportCat
 * @see ReportCatRepository
 */
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
        log.info("Was invoked method to add a reportCat");
        return this.reportCatRepository.save(reportCat);
    }

    /**
     * Метод находит и возвращает отчет по id
     * @param id
     * @return {@link ReportCatRepository#findById(Object)}
     * @throws ReportCatNotFoundException если отчет с указанным id не найден
     * @see ReportCatService
     */
    public ReportCat get(Long id) {
        log.info("Was invoked method to get a reportCat by id={}", id);
        return this.reportCatRepository.findById(id)
                .orElseThrow(ReportCatNotFoundException::new);
    }

    /**
     * Метод находит и удаляет отчет по id
     * @param id
     * @throws ReportCatNotFoundException если отчет с указанным id не найден
     * @see ReportCatService
     */
    public boolean remove(Long id) {
        log.info("Was invoked method to remove a reportCat by id={}", id);
        if (reportCatRepository.existsById(id)) {
            reportCatRepository.deleteById(id);
            return true;
        }
        throw new ReportCatNotFoundException();
    }

    /**
     * Метод обновляет и возвращает отчет
     * @param reportCat
     * @return {@link ReportCatRepository#save(Object)}
     * @throws ReportCatNotFoundException если отчет с указанным id не найден
     * @see ReportCatService
     */
    public ReportCat update(ReportCat reportCat) {
        log.info("Was invoked method to upload a reportCat");
        if (reportCat.getId() != null && get(reportCat.getId()) != null) {
            ReportCat findCat = get(reportCat.getId());
            findCat.setReportDate(reportCat.getReportDate());
            findCat.setFileId(reportCat.getFileId());
            findCat.setCaption(reportCat.getCaption());
            findCat.setExamination(reportCat.getExamination());
            return this.reportCatRepository.save(findCat);
        }
        throw new ReportCatNotFoundException();
    }

    /**
     * Метод находит и возвращает все отчеты
     * @return {@link ReportCatRepository#findAll()}
     * @see ReportCatService
     */
    public Collection<ReportCat> getAll() {
        log.info("Was invoked method to get all reportsCat");
        return this.reportCatRepository.findAll();
    }
}
