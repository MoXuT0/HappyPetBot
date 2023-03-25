package com.team4.happydogbot.service;

import com.team4.happydogbot.config.BotConfig;
import com.team4.happydogbot.entity.ReportCat;
import com.team4.happydogbot.exception.ReportCatNotFoundException;
import com.team4.happydogbot.repository.ReportCatRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

    private final BotConfig config;

    public ReportCatService(ReportCatRepository reportRepository, BotConfig config) {
        this.reportCatRepository = reportRepository;
        this.config = config;
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

    /**
     * Метод находит в базе данных fileId фотографии к отчету, делает запрос Telegram на получение filePath фотографии,
     * получает фотографию, получает byte фотографии
     * @param id
     * @return byte фотографии
     */
    public byte[] getFile(Long id) {
        String fileId = reportCatRepository.getReferenceById(id).getFileId();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                config.getFileInfoUri(),
                HttpMethod.GET,
                request,
                String.class,
                config.getToken(), fileId
        );
        JSONObject responseJson = new JSONObject(response.getBody());
        JSONObject pathJson = responseJson.getJSONObject("result");
        String filePath = pathJson.getString("file_path");
        String fullUri = config.getFileStorageUri()
                .replace("{token}", config.getToken())
                .replace("{filePath}", filePath);
        URL urlObj;
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
