package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.entity.ReportDog;
import com.team4.happydogbot.exception.ReportDogNotFoundException;
import com.team4.happydogbot.repository.ReportDogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

    private final BotConfig config;

    public ReportDogService(ReportDogRepository reportRepository, BotConfig config) {
        this.reportDogRepository = reportRepository;
        this.config = config;
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
            return this.reportRepository.save(findDog);
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

    public byte[] getFile(Long id) {
        String fileId = reportDogRepository.getReferenceById(id).getFileId();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> filePath = restTemplate.exchange(
                config.getFileInfoUri(),
                HttpMethod.GET,
                request,
                String.class,
                config.getToken(), fileId
        );

        String fullUri = config.getFileStorageUri()
                .replace("{token}", config.getToken())
                .replace("{filePath}", "photos/file_1.jpg");

        URL urlObj = null;
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Файл не скачен");
        }

        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        GetFile getFileMethod = new GetFile();
//        getFileMethod.setFileId(fileId);
//        try {
//            File file = execute(getFileMethod);
//            return downloadFile(file.getFilePath());
////            byte[] image = bot.getBaseUrl()
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//        throw new ReportDogNotFoundException();
    }
}
