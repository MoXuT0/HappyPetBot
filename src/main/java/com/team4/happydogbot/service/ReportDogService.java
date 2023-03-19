package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.ReportDog;
import com.team4.happydogbot.exception.ReportDogNotFoundException;
import com.team4.happydogbot.repository.ReportDogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

/**
 *Класс - сервис, содержащий набор CRUD операций над объектом ReportDog
 * @see ReportDog
 * @see ReportDogRepository
 */
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
        log.info("Was invoked method to add a report");

        return this.reportRepository.save(reportDog);
    }

    /**
     * Метод находит и возвращает отчет по id
     * @param id
     * @return {@link ReportDogRepository#findById(Object)}
     * @throws ReportDogNotFoundException если отчет с указанным id не найден
     * @see ReportDogService
     */
    public ReportDog get(Long id) {
        log.info("Was invoked method to get a report by id={}", id);


        return this.reportRepository.findById(id)
                .orElseThrow(ReportDogNotFoundException::new);
    }

    /**
     * Метод находит и удаляет отчет по id
     * @param id
     * @return true если удаление прошло успешно
     * @throws ReportDogNotFoundException если отчет с указанным id не найден
     * @see ReportDogService
     */
    public boolean remove(Long id) {
        log.info("Was invoked method to remove a report by id={}", id);

        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return true;
        }
        throw new ReportDogNotFoundException();
    }

    /**
     * Метод обновляет и возвращает отчет
     * @param reportDog
     * @return {@link ReportDogRepository#save(Object)}
     * @throws ReportDogNotFoundException если отчет с указанным id не найден
     * @see ReportDogService
     */
    public Optional<ReportDog> update(ReportDog reportDog) {
        log.info("Was invoked method to upload a reportDog");

        if (reportRepository.existsById(reportDog.getId())) {
            return Optional.ofNullable(reportRepository.save(reportDog));
        }
        throw new ReportDogNotFoundException();
    }

    /**
     * Метод находит и возвращает все отчеты
     * @return {@link ReportDogRepository#findAll()}
     * @see ReportDogService
     */
    public Collection<ReportDog> getAll() {
        log.info("Was invoked method to get all reportsDog");

        return this.reportRepository.findAll();
    }
}
