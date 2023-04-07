package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.ReportDog;
import com.team4.happydogbot.exception.ReportDogNotFoundException;
import com.team4.happydogbot.repository.ReportDogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

/**
 *Класс - сервис, содержащий набор CRUD операций над объектом ReportDog
 * @see ReportDog
 * @see ReportDogRepository
 */
@Slf4j
@Service
@Transactional
public class ReportDogService {
    private final ReportDogRepository reportDogRepository;
    private final Bot bot;

    public ReportDogService(ReportDogRepository reportRepository, Bot bot) {
        this.reportDogRepository = reportRepository;
        this.bot = bot;
    }

    /**
     * Метод сохраняет отчет пользователя
     * @param reportDog
     * @return {@link ReportDogRepository#save(Object)}
     * @see ReportDogService
     */
    public ReportDog add(ReportDog reportDog) {
        log.info("Was invoked method to add a report");

        return this.reportDogRepository.save(reportDog);
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

        return this.reportDogRepository.findById(id)
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

        if (reportDogRepository.existsById(id)) {
            reportDogRepository.deleteById(id);
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
    public ReportDog update(ReportDog reportDog) {
        log.info("Was invoked method to upload a reportDog");

        if (reportDog.getId() != null && get(reportDog.getId()) != null) {
            ReportDog findDog = get(reportDog.getId());
            findDog.setReportDate(reportDog.getReportDate());
            findDog.setFileId(reportDog.getFileId());
            findDog.setCaption(reportDog.getCaption());
            findDog.setExamination(reportDog.getExamination());
            return this.reportDogRepository.save(findDog);
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

        return this.reportDogRepository.findAll();
    }

    /**
     * Метод находит в базе данных fileId фотографии к отчету, делает запрос Telegram на получение файла фотографии,
     * получает фотографию, читает и возвращает byte фотографии
     * @param id
     * @return byte фотографии
     */
    public byte[] getFile(Long id) {
        log.info("Was invoked method to get a photo of the report by id={}", id);

        String fileId = reportDogRepository.getReferenceById(id).getFileId();
        try {
            File file = bot.execute(GetFile.builder().fileId(fileId).build());
            java.io.File file1 = bot.downloadFile(file);
            return Files.readAllBytes(file1.toPath());
        } catch (TelegramApiException | IOException e) {
            throw new ReportDogNotFoundException();
        }
    }
}
